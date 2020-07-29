package net.kamradt.pfm

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductFileMapperTest {
    val logger = LoggerFactory.getLogger(javaClass)
    val sut = ProductFileMapper(
        producer = ProductReaderProducer(),
        storeMapperMap = mapOf("test-store" to SimpleStoreMapper()),
        consumer = MapProductDescriptionConsumer()
    )
    @Test
    fun `Process a simple test file`() = runBlocking {
        sut.mapProductReader(
            BufferedReader(
                InputStreamReader(
                    javaClass.getResourceAsStream("/testfile.txt"))),
            "test-store"
        )
        val consumer: MapProductDescriptionConsumer
                = sut.consumer as MapProductDescriptionConsumer
        assertEquals(3, consumer.map.size)
        val keys = listOf(1L, 2L, 3L)
        consumer.map.forEach {
            assertTrue(keys.contains(it.key))
            assertEquals(it.key, it.value.productId)
            assertEquals("line", it.value.productDescription)
            logger.info("found product description $it.value")
        }
    }
    @Test
    fun `Process a simple test file with bad row`() = runBlocking {
        sut.mapProductReader(
            BufferedReader(
                InputStreamReader(
                    javaClass.getResourceAsStream("/testfilebadrow.txt"))),
            "test-store"
        )
        val consumer: MapProductDescriptionConsumer
                = sut.consumer as MapProductDescriptionConsumer
        assertEquals(3, consumer.map.size)
        val keys = listOf(1L, 2L, 3L)
        consumer.map.forEach {
            assertTrue(keys.contains(it.key))
            assertEquals(it.key, it.value.productId)
            assertEquals("line", it.value.productDescription)
        }
    }
}