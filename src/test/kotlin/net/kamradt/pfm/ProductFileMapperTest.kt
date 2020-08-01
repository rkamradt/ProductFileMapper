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
