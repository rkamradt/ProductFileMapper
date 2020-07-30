package net.kamradt.pfm

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductFileMapperTest {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val consumer = MapProductDescriptionConsumer()

    @Test
    fun `Process a simple test file`() = runBlocking {
        val sut = ProductFileMapper(
            storeMapperMap = mapOf("test-store" to SimpleStoreMapper()),
            consumer = consumer
        )
        sut.mapProductReader(
            BufferedReader(
                InputStreamReader(
                    javaClass.getResourceAsStream("/testfile.txt"))),
            "test-store"
        )
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
        val sut = ProductFileMapper(
            storeMapperMap = mapOf("test-store" to SimpleStoreMapper()),
            consumer = consumer
        )
        sut.mapProductReader(
            BufferedReader(
                InputStreamReader(
                    javaClass.getResourceAsStream("/testfilebadrow.txt"))),
            "test-store"
        )
        assertEquals(3, consumer.map.size)
        val keys = listOf(1L, 2L, 3L)
        consumer.map.forEach {
            assertTrue(keys.contains(it.key))
            assertEquals(it.key, it.value.productId)
            assertEquals("line", it.value.productDescription)
        }
    }

    @Test
    fun `Process a complex test file that matches configuration for SuperStore`() = runBlocking {
        val sut = ProductFileMapper(
            consumer = consumer
        )
        sut.mapProductReader(
            BufferedReader(
                InputStreamReader(
                    javaClass.getResourceAsStream("/testfile.txt"))),
            "SuperStore"
        )
        assertEquals(0, consumer.map.size)
    }
}