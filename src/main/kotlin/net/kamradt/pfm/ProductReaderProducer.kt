package net.kamradt.pfm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import java.io.BufferedReader

class ProductReaderProducer {
    fun recordProducer(reader: BufferedReader): Flow<String> {
        return reader.lineSequence().asFlow()
    }

}
