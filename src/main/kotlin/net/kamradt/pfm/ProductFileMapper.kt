package net.kamradt.pfm

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import net.kamradt.pfm.api.ProductDescriptionConsumer
import net.kamradt.pfm.api.StoreMapper
import net.kamradt.pfm.data.ProductDescription
import net.kamradt.pfm.data.StoreFileDescriptor
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.RuntimeException
import java.math.BigDecimal
import java.util.Properties

class ProductFileMapper(
    private val consumer: ProductDescriptionConsumer,
    private val producer: ProductReaderProducer =
        ProductReaderProducer(),
    private val storeMapperMap: Map<String, StoreMapper> =
        configStoreMapBuilder()
) {

    suspend fun mapProductFile(file: File, storeName: String) {
        val reader = BufferedReader(FileReader(file))
        reader.use {
            mapProductReader(reader, storeName)
        }
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

data class StoresFileDescriptor(
    val type: String,
    val stores: List<StoreFileDescriptor>
)

fun configStoreMapBuilder(): Map<String, StoreMapper> {
    val mapper = ObjectMapper(YAMLFactory())
    mapper.registerModule(KotlinModule())
    val map = ProductFileMapper::class.java.getResourceAsStream("/stores.yaml").use {
        val stores: StoresFileDescriptor = mapper.readValue(it)
        return stores.stores.associateBy({it.name}, {createStoreMapper(it)})
    }
}

fun createStoreMapper(descriptor: StoreFileDescriptor): StoreMapper =
    object : StoreMapper {
        override fun mapFromStore(row: String): Map<String, String>? =
            try {
                descriptor.fields.associateBy({it.storeFileFieldName},{row.substring(it.startOffset,it.endOffset)})
            } catch (ex: Exception) {
                null
            }

        override fun mapToProduct(storeData: Map<String, String>): ProductDescription? =
            try {
                ProductDescription(
                    productId = descriptor.convertLong("productId", storeData),
                    productDescription = descriptor.convertOptString("productDescription", storeData),
                    regularDisplayPrice = descriptor.convertOptString("regularDisplayPrice", storeData),
                    regularCalculatorPrice = descriptor.convertBigDecimal("regularCalculatorPrice", storeData),
                    promotionalDisplayPrice = descriptor.convertOptString("promotionalDisplayPrice", storeData),
                    promotionalCalculatorPrice = descriptor.convertOptBigDecimal("promotionalCalculatorPrice", storeData),
                    unitOfMeasure = descriptor.convertOptString("unitOfMeasure", storeData),
                    productSize = descriptor.convertOptString("productSize", storeData),
                    taxRate = descriptor.convertBigDecimal("taxRate", storeData)
                )
            } catch (ex: Exception) {
                null
            }
        }

