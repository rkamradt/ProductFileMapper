/*
 * The MIT License
 *
 * Copyright 2020 randalkamradt.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.kamradt.pfm

import kotlinx.coroutines.runBlocking
import net.kamradt.pfm.data.ProductDescription
import org.junit.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ProductFileMapperTest {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val productMap = mutableMapOf<Long, ProductDescription>()

    @Test
    fun `Process a test file that matches configuration for SuperStore`() = runBlocking {
        val sut = ProductFileMapper(
            destination = {
                productMap[it.productId] = it
            }
        )
        runBlocking {
            sut.mapProductReader(
                BufferedReader(
                    InputStreamReader(
                        javaClass.getResourceAsStream("/SuperStore.txt")
                    )
                ),
                "SuperStore"
            )
        }
        assertEquals(4, productMap.size)
        assertTrue(productMap.containsKey(80000001L))
        assertTrue(productMap.containsKey(14963801L))
        assertTrue(productMap.containsKey(40123401L))
        assertTrue(productMap.containsKey(50133333L))
        val apples = productMap[50133333];
        assertEquals(50133333L, apples?.productId)
        assertEquals("Fuji Apples (Organic)", apples?.productDescription)
        assertEquals("\$3.49", apples?.regularDisplayPrice)
        assertEquals(BigDecimal.valueOf(34900, 4), apples?.regularCalculatorPrice)
        assertEquals("\$0.00", apples?.promotionalDisplayPrice)
        assertEquals(BigDecimal.valueOf(0, 4), apples?.promotionalCalculatorPrice)
        assertEquals("Pound", apples?.unitOfMeasure)
        assertEquals("lb", apples?.productSize)
        val marlboro = productMap[40123401L];
        assertEquals(40123401L, marlboro?.productId)
        assertEquals("Marlboro Cigarettes", marlboro?.productDescription)
        assertEquals("\$10.00", marlboro?.regularDisplayPrice)
        assertEquals(BigDecimal.valueOf(100000, 4), marlboro?.regularCalculatorPrice)
        assertEquals("\$5.49", marlboro?.promotionalDisplayPrice)
        assertEquals(BigDecimal.valueOf(54900, 4), marlboro?.promotionalCalculatorPrice)
        assertEquals("Each", marlboro?.unitOfMeasure)
        assertEquals("", marlboro?.productSize)
        val soda = productMap[14963801L];
        assertEquals(14963801L, soda?.productId)
        assertEquals("Generic Soda 12-pack", soda?.productDescription)
        assertEquals("2 for \$13.00", soda?.regularDisplayPrice)
        assertEquals(BigDecimal.valueOf(65000, 4), soda?.regularCalculatorPrice)
        assertEquals("\$5.49", soda?.promotionalDisplayPrice)
        assertEquals(BigDecimal.valueOf(54900, 4), soda?.promotionalCalculatorPrice)
        assertEquals("Each", soda?.unitOfMeasure)
        assertEquals("12x12oz", soda?.productSize)
        val rice = productMap[80000001L];
        assertEquals(80000001L, rice?.productId)
        assertEquals("Kimchi-flavored white rice", rice?.productDescription)
        assertEquals("\$5.67", rice?.regularDisplayPrice)
        assertEquals(BigDecimal.valueOf(56700, 4), rice?.regularCalculatorPrice)
        assertEquals("\$0.00", rice?.promotionalDisplayPrice)
        assertEquals(BigDecimal.valueOf(0, 4), rice?.promotionalCalculatorPrice)
        assertEquals("Each", rice?.unitOfMeasure)
        assertEquals("18oz", rice?.productSize)
    }

    @Test
    fun `test createStoreMapBuilder function with stores yaml`() {
        val storeMap = configStoreMapBuilder("/stores.yaml")
        assertEquals(1, storeMap.size)
        assertTrue(storeMap.containsKey("SuperStore"))
    }

    @Test
    fun `test createStoreMapBuilder function for boundry condition`() {
        val sut = ProductFileMapper(
            destination = {
                productMap[it.productId] = it
            }
        )
        runBlocking {
            sut.mapProductReader(
                BufferedReader(
                    InputStreamReader(
                        javaClass.getResourceAsStream("/boundries.txt")
                    )
                ),
                "SuperStore"
            )
        }
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

    fun `test of readProductFromFile standalone`() {
        val list = readProductFromFile(File("target/SuperStore.txt"), "SuperStore")
        assertEquals(4, list.size)
        assertTrue(productMap.containsKey(80000001L))
        assertTrue(productMap.containsKey(14963801L))
        assertTrue(productMap.containsKey(40123401L))
        assertTrue(productMap.containsKey(50133333L))
    }
}
