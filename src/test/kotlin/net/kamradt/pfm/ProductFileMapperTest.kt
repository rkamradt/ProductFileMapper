package net.kamradt.pfm

import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNull
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
                    javaClass.getResourceAsStream("/SuperStore.txt"))),
            "SuperStore"
        )
        assertEquals(3, consumer.map.size)
    }

    @Test
    fun `test createStoreMapBuilder function with teststore yaml`() {
        val storeMap = configStoreMapBuilder("/teststore.yaml")
        assertEquals(2, storeMap.size)
        assertTrue(storeMap.containsKey("SuperStore"))
        assertTrue(storeMap.containsKey("MediocreStore"))
    }

    @Test
    fun `test createStoreMapBuilder function with stores yaml`() {
        val storeMap = configStoreMapBuilder("/stores.yaml")
        assertEquals(1, storeMap.size)
        assertTrue(storeMap.containsKey("SuperStore"))
    }

    @Test
    fun `test createStoreMapBuilder function for boundry condition`() = runBlocking {
        val sut = ProductFileMapper(
            consumer = consumer
        )
        sut.mapProductReader(
            BufferedReader(
                InputStreamReader(
                    javaClass.getResourceAsStream("/boundries.txt"))),
            "SuperStore"
        )
        //TODO fix conversion assertions
        assertEquals(1, consumer.map.size)
        assertEquals(12345678L, consumer.map[12345678L]?.productId)
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&", consumer.map[12345678L]?.productDescription)
        assertEquals("\$123456.78", consumer.map[12345678L]?.regularDisplayPrice)
        assertEquals(BigDecimal.valueOf(0,4), consumer.map[12345678L]?.regularCalculatorPrice)
        assertEquals("12345678 for \$123456.78", consumer.map[12345678L]?.promotionalDisplayPrice)
        assertNull(consumer.map[12345678L]?.promotionalCalculatorPrice)
        assertEquals("Each", consumer.map[12345678L]?.unitOfMeasure)
        assertNull(consumer.map[12345678L]?.productSize)

    }
}