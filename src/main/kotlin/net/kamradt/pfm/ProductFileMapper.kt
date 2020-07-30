package net.kamradt.pfm

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import net.kamradt.pfm.api.ProductDescriptionConsumer
import net.kamradt.pfm.api.StoreMapper
import net.kamradt.pfm.data.ProductDescription
import net.kamradt.pfm.data.StoreFileDescriptor
import net.kamradt.pfm.data.StoresFileDescriptor
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.RuntimeException
import java.util.Objects.isNull

class ProductFileMapper(
    private val consumer: ProductDescriptionConsumer,
    private val producer: ProductReaderProducer =
        ProductReaderProducer(),
    private val storeMapperMap: Map<String, StoreMapper> =
        configStoreMapBuilder()
) {

    suspend fun mapProductFile(file: File, storeName: String) =
        BufferedReader(FileReader(file)).use {
        mapProductReader(it, storeName)
    }

    suspend fun mapProductReader(reader: BufferedReader, storeName: String): Unit =
        if (storeMapperMap.containsKey(storeName))
            producer.recordProducer(reader)
                .map { storeMapperMap.getValue(storeName).mapFromStore(it) }
                .filterNotNull()
                .map { storeMapperMap.getValue(storeName).mapToProduct(it) }
                .filterNotNull()
                .collect { consumer.action(it) }
        else
            throw RuntimeException("unknown store $storeName")
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
        override fun mapFromStore(row: String): Map<String, String>? =
            try {
                val maxRowSize = descriptor
                    .fields
                    .stream()
                    .mapToInt{ it.endOffset ?: 0 }
                    .max()
                    .orElse(0);
                if (row.length < maxRowSize) row.padEnd(maxRowSize,' ')
                descriptor.fields
                    .filter { !isNull(it.storeFileFieldName)  }
                    .associateBy({it.storeFileFieldName ?: ""},
                        {row.substring((it.startOffset ?: 1)-1,(it.endOffset ?: 1))})
            } catch (ex: Exception) {
                null
            }

        override fun mapToProduct(storeData: Map<String, String>): ProductDescription? =
            try {
                ProductDescription(
                    productId = descriptor.convertLong("productId", storeData, descriptor),
                    productDescription = descriptor.convertOptString("productDescription", storeData, descriptor),
                    regularDisplayPrice = descriptor.convertOptString("regularDisplayPrice", storeData, descriptor),
                    regularCalculatorPrice = descriptor.convertBigDecimal("regularCalculatorPrice", storeData, descriptor),
                    promotionalDisplayPrice = descriptor.convertOptString("promotionalDisplayPrice", storeData, descriptor),
                    promotionalCalculatorPrice = descriptor.convertOptBigDecimal("promotionalCalculatorPrice", storeData, descriptor),
                    unitOfMeasure = descriptor.convertOptString("unitOfMeasure", storeData, descriptor),
                    productSize = descriptor.convertOptString("productSize", storeData, descriptor),
                    taxRate = descriptor.convertBigDecimal("taxRate", storeData, descriptor)
                )
            } catch (ex: Exception) {
                null
            }
        }

