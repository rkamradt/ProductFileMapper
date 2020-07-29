package net.kamradt.pfm

import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductReaderProducerTest {
    val logger = LoggerFactory.getLogger(javaClass)
    val sut = ProductReaderProducer()
    @Test
    fun `Read a test file`() = runBlocking {
        val reader = BufferedReader(InputStreamReader(this.javaClass.getResourceAsStream("/testfile.txt")))
        reader.use {
            val set = sut.recordProducer(reader)
                .toSet()
            assertEquals(3, set.size)
            set.forEach {
                assertTrue(it.startsWith("line"))
                logger.info("found line $it")
            }
        }
    }
}