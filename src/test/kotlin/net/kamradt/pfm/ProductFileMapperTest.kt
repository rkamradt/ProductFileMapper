package net.kamradt.pfm

import kotlinx.coroutines.runBlocking
import net.kamradt.pfm.data.ProductDescription
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductFileMapperTest {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val productMap = mutableMapOf<Long, ProductDescription>()

    @Test
    fun `Process a complex test file that matches configuration for SuperStore`() = runBlocking {
        val sut = ProductFileMapper(
            destination = {
                productMap[it.productId] = it
            }
        )
        sut.mapProductReader(
            BufferedReader(
                InputStreamReader(
                    javaClass.getResourceAsStream("/SuperStore.txt")
                )
            ),
            "SuperStore"
        )
        assertEquals(4, productMap.size)
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
            destination = {
                productMap[it.productId] = it
            }
        )
        sut.mapProductReader(
            BufferedReader(
                InputStreamReader(
                    javaClass.getResourceAsStream("/boundries.txt")
                )
            ),
            "SuperStore"
        )
        assertEquals(1, productMap.size)
        assertEquals(12345678L, productMap[12345678L]?.productId)
        assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&", productMap[12345678L]?.productDescription)
        assertEquals("12345678 for \$123456.78", productMap[12345678L]?.regularDisplayPrice)
        assertEquals(BigDecimal.valueOf(100, 4), productMap[12345678L]?.regularCalculatorPrice)
        assertEquals("12345678 for \$123456.78", productMap[12345678L]?.promotionalDisplayPrice)
        assertEquals(BigDecimal.valueOf(100, 4), productMap[12345678L]?.promotionalCalculatorPrice)
        assertEquals("Each", productMap[12345678L]?.unitOfMeasure)
        assertEquals("12x12oz", productMap[12345678L]?.productSize)
    }
}
