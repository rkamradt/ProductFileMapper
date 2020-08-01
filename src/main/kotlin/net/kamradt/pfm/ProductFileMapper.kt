/*
 * The MIT License
 *
 * Copyright 2020 randalkamradt.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.kamradt.pfm

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.api.StoreMapper
import net.kamradt.pfm.data.MAX_ROW_SIZE
import net.kamradt.pfm.data.ProductDescription
import net.kamradt.pfm.data.StoreFileDescriptor
import net.kamradt.pfm.data.StoreFileDescriptorField
import net.kamradt.pfm.data.StoresFileDescriptor
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.nio.charset.StandardCharsets
import java.util.Objects.isNull
import kotlin.reflect.full.createInstance

/**
 * example usage of the ProductFileMapper. Runs mapProductReader blocking and
 * collects the ProductDescription objects in a MutableList.
 */
fun readProductFromFile(file: File, storeName: String): List<ProductDescription> {
    val list = mutableListOf<ProductDescription>()
    val mapper = ProductFileMapper(
        destination = { list.add(it) }
    )
    val reader = BufferedReader(FileReader(file, StandardCharsets.UTF_8))
    reader.use { // close the reader when we're done
        runBlocking { // block until the method is complete
            mapper.mapProductReader(reader, storeName)

        }
    }
    return list
}

/**
 * The main class for this library. The destination parameter is expected
 * to enter items into a database. The source and storeMapperMap are
 * defaulted with reasonable values
 *
 */
class ProductFileMapper(
    private val destination: (ProductDescription) -> Unit,
    private val source: (BufferedReader) -> Flow<String> =
        { it.lineSequence().asFlow() },
    private val storeMapperMap: Map<String, StoreMapper> =
        configStoreMapBuilder()
) {
    /**
     * The main function for this class. Will read from the reader parameters,
     * map the values into a ProductDescription, and send them to the destination.
     *
     * The storeName parameter will find the store in the stores.yaml and use that
     * for mapping instructions. Currently only 'SuperStore' is used.
     */
    suspend fun mapProductReader(reader: BufferedReader, storeName: String): Unit =
        if (storeMapperMap.containsKey(storeName)) {
            val storeMapper = storeMapperMap[storeName]
            source(reader)
                .map { storeMapper?.mapFromStore(it) }
                .filterNotNull()
                .map { storeMapper?.mapToProduct(it) }
                .filterNotNull()
                .collect { destination(it) }
        } else
            throw IllegalArgumentException("unknown store $storeName")
}

/**
 * read in the stores from 'stores.yaml' and build up a map of StoreMapper objects
 */
fun configStoreMapBuilder(resourceName: String = "/stores.yaml"): Map<String, StoreMapper> {
    val mapper = ObjectMapper(YAMLFactory())
    mapper.registerModule(KotlinModule())
    ProductFileMapper::class.java.getResourceAsStream(resourceName).use {
        val stores: StoresFileDescriptor = mapper.readValue(it)
        return stores.stores.associateBy({ it.name }, { createStoreMapper(it) })
    }
}

/**
 * creates a StoreMapper function with both phases of the mapping defined
 * by the StoreFileDescriptor which is read in from the stores.yaml. One per
 * store (although there's only one store for now)
 */
fun createStoreMapper(descriptor: StoreFileDescriptor): StoreMapper =
    object : StoreMapper {
        val converterMap: Map<String, StoreFileDescriptorConverter<Any>> =
            descriptor.fields
                .filter {
                    !isNull(it.converterClassName) &&
                        !isNull(it.productDescriptionField)
                }
                .associateBy(
                    { it.productDescriptionField ?: "" },
                    { getConverterClass(it) }
                )
        override fun mapFromStore(row: String): Map<String, String>? =
            try {
                val paddedrow = if (row.length < MAX_ROW_SIZE)
                    row.padEnd(MAX_ROW_SIZE, ' ')
                else
                    row
                descriptor.fields
                    .filter { !isNull(it.storeFileFieldName) }
                    .associateBy(
                        { it.storeFileFieldName ?: "" },
                        { paddedrow.substring((it.startOffset ?: 1) - 1, (it.endOffset ?: 1)) }
                    )
            } catch (ex: Exception) {
                null // nulls are filtered out so invalid rows are ignored
            }

        override fun mapToProduct(storeData: Map<String, String>): ProductDescription? =
            try {
                ProductDescription(
                    converterMap,
                    storeData,
                    descriptor
                )
            } catch (ex: Exception) {
                null // nulls are filtered out so invalid rows are ignored
            }
    }

/**
 * helper function to find converters from their class names as strings
 */
@Suppress("UNCHECKED_CAST")
fun getConverterClass(descriptor: StoreFileDescriptorField): StoreFileDescriptorConverter<Any> =
    Class.forName(descriptor.converterClassName)
        .kotlin
        .createInstance() as StoreFileDescriptorConverter<Any>
