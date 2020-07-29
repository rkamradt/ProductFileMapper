package net.kamradt.pfm

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.RuntimeException

class ProductFileMapper(
    val producer: ProductReaderProducer,
    val mapper: Map<String, StoreMapper>,
    val consumer: ProductDescriptionConsumer
) {

    suspend fun mapProductFile(file: File, storeName: String) {
        val reader = BufferedReader(FileReader(file))
        reader.use {
            mapProductReader(reader, storeName)
        }
    }

    suspend fun mapProductReader(reader: BufferedReader, storeName: String): Unit =
        if (mapper.containsKey(storeName))
            producer.recordProducer(reader)
                .map { mapper.getValue(storeName).mapFromStore(it) }
                .filterNotNull()
                .collect { consumer.action(it) }
        else
            throw RuntimeException("unknown store $storeName")
}

