package net.kamradt.pfm

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.flow.*
import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.api.StoreMapper
import net.kamradt.pfm.data.*
import java.io.BufferedReader
import java.lang.RuntimeException
import java.util.Objects.isNull
import kotlin.reflect.full.createInstance

class ProductFileMapper(
    private val destination: (ProductDescription) -> Unit,
    private val source: (BufferedReader) -> Flow<String> =
        { reader -> reader.lineSequence().asFlow() },
    private val storeMapperMap: Map<String, StoreMapper> =
        configStoreMapBuilder()
) {

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

fun configStoreMapBuilder(resourceName: String = "/stores.yaml"): Map<String, StoreMapper> {
    val mapper = ObjectMapper(YAMLFactory())
    mapper.registerModule(KotlinModule())
    ProductFileMapper::class.java.getResourceAsStream(resourceName).use {
        val stores: StoresFileDescriptor = mapper.readValue(it)
        return stores.stores.associateBy({it.name}, {createStoreMapper(it)})
    }
}


fun createStoreMapper(descriptor: StoreFileDescriptor): StoreMapper =
    object : StoreMapper {
        val converterMap: Map<String, StoreFileDescriptorConverter<Any>> =
            descriptor.fields
                .filter { !isNull(it.converterClassName)
                        && !isNull(it.productDescriptionField)}
                .associateBy({it.productDescriptionField ?: ""},
                    { getConverterClass(it) })
        override fun mapFromStore(row: String): Map<String, String>? =
            try {
                val paddedrow = if (row.length < MAX_ROW_SIZE)
                    row.padEnd(MAX_ROW_SIZE,' ')
                else
                    row
                descriptor.fields
                    .filter { !isNull(it.storeFileFieldName)  }
                    .associateBy({it.storeFileFieldName ?: ""},
                        {paddedrow.substring((it.startOffset ?: 1)-1,(it.endOffset ?: 1))})
            } catch (ex: Exception) {
                null
            }

        override fun mapToProduct(storeData: Map<String, String>): ProductDescription? =
            try {
                ProductDescription(
                    converterMap,
                    storeData,
                    descriptor
                )
            } catch (ex: Exception) {
                null
            }
        }

fun getConverterClass(descriptor: StoreFileDescriptorField): StoreFileDescriptorConverter<Any> =
    Class.forName(descriptor.converterClassName)
        .kotlin
        .createInstance() as StoreFileDescriptorConverter<Any>
