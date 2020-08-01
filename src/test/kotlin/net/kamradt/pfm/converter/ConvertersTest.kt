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
package net.kamradt.pfm.converter

import net.kamradt.pfm.data.EACH
import net.kamradt.pfm.data.POUND
import net.kamradt.pfm.data.StoreFileDescriptor
import net.kamradt.pfm.data.storeFlags
import net.kamradt.pfm.data.storePromotionalForX
import net.kamradt.pfm.data.storePromotionalSingularPrice
import net.kamradt.pfm.data.storePromotionalSplitPrice
import net.kamradt.pfm.data.storeRegularForX
import net.kamradt.pfm.data.storeRegularSingularPrice
import net.kamradt.pfm.data.storeRegularSplitPrice
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ConvertersTest {

    private val descriptor = StoreFileDescriptor(
        "storeName", // we don't use the StoreFileDescriptor in the standard converters
        listOf()
    )

    @Test
    fun `test the OptionalString converter`() {
        val randomString = "abcedfg"
        val sut = OptionalString()
        val map = mapOf("field" to randomString)
        val converted = sut.convert("field", map, descriptor)
        assertEquals(randomString, converted)
        val shouldBeNull = sut.convert("non-existent-field", map, descriptor)
        assertNull(shouldBeNull)
    }

    @Test
    fun `test the Number converter`() {
        val randomNumber = "12345678"
        val sut = Number()
        val map = mapOf("field" to randomNumber)
        val converted = sut.convert("field", map, descriptor)
        assertEquals(randomNumber.toLong(), converted)
    }

    @Test
    fun `test the PromotionalDisplayPrice converter in split mode`() {
        val randomNumber = "12345678"
        val sut = PromotionalDisplayPrice()
        val map = mapOf(
            storePromotionalSplitPrice to randomNumber,
            storePromotionalForX to "3"
        )
        val converted = sut.convert("", map, descriptor)
        val price = BigDecimal.valueOf(randomNumber.toLong(), 2)
        assertEquals("3 for \$$price", converted)
    }

    @Test
    fun `test the PromotionalDisplayPrice converter in singular mode`() {
        val randomNumber = "12345678"
        val sut = PromotionalDisplayPrice()
        val map = mapOf(storePromotionalSingularPrice to randomNumber)
        val converted = sut.convert("", map, descriptor)
        val price = BigDecimal.valueOf(randomNumber.toLong(), 2)
        assertEquals("\$$price", converted)
    }

    @Test
    fun `test the RegularDisplayPrice converter in split mode`() {
        val randomNumber = "12345678"
        val sut = RegularDisplayPrice()
        val map = mapOf(
            storeRegularSplitPrice to randomNumber,
            storeRegularSingularPrice to "00000000",
            storeRegularForX to "00000003"
        )
        val converted = sut.convert("field", map, descriptor)
        val price = BigDecimal.valueOf(randomNumber.toLong(), 2)
        assertEquals("3 for \$$price", converted)
    }

    @Test
    fun `test the RegularDisplayPrice converter in singular mode`() {
        val randomNumber = "12345678"
        val sut = RegularDisplayPrice()
        val map = mapOf(
            storeRegularSingularPrice to randomNumber,
            storeRegularSplitPrice to "00000000"
        )
        val converted = sut.convert("field", map, descriptor)
        val price = BigDecimal.valueOf(randomNumber.toLong(), 2)
        assertEquals("\$${price ?: 0}", converted)
    }

    @Test
    fun `test the PromotionalCalculatedPrice converter in split mode`() {
        val randomNumber = "12345678"
        val sut = PromotionalCalculatedPrice()
        val map = mapOf(
            storePromotionalSplitPrice to randomNumber,
            storePromotionalForX to "3"
        )
        val converted = sut.convert("", map, descriptor)
        val price = BigDecimal.valueOf(randomNumber.toLong(), 2).setScale(4)
        val expected = price.divide(BigDecimal.valueOf(3), RoundingMode.HALF_DOWN)
        assertEquals(expected, converted)
    }

    @Test
    fun `test the PromotionalCalculatedPrice converter in singular mode`() {
        val randomNumber = "12345678"
        val sut = PromotionalCalculatedPrice()
        val map = mapOf(storePromotionalSingularPrice to randomNumber)
        val converted = sut.convert("", map, descriptor)
        val price = BigDecimal.valueOf(randomNumber.toLong(), 2).setScale(4)
        assertEquals(price, converted)
    }

    @Test
    fun `test the RegularCalculatedPrice converter in split mode`() {
        val randomNumber = "12345678"
        val sut = RegularCalculatedPrice()
        val map = mapOf(
            storeRegularSplitPrice to randomNumber,
            storeRegularSingularPrice to "00000000",
            storeRegularForX to "00000004"
        )
        val converted = sut.convert("field", map, descriptor)
        val price = BigDecimal.valueOf(randomNumber.toLong(), 2).setScale(4)
        val expected = price.divide(BigDecimal.valueOf(4), RoundingMode.HALF_DOWN)
        assertEquals(expected, converted)
    }

    @Test
    fun `test the RegularCalculatedPrice converter in singular mode`() {
        val randomNumber = "12345678"
        val sut = RegularCalculatedPrice()
        val map = mapOf(
            storeRegularSingularPrice to randomNumber,
            storeRegularSplitPrice to "00000000"
        )
        val converted = sut.convert("field", map, descriptor)
        val price = BigDecimal.valueOf(randomNumber.toLong(), 2).setScale(4)
        assertEquals(price, converted)
    }

    @Test
    fun `test the TaxRate converter`() {
        val taxFlag = "NNNNYNNN"
        val sut = TaxRate()
        val map = mapOf("flags" to taxFlag)
        val converted = sut.convert("", map, descriptor)
        assertEquals(BigDecimal.valueOf(7775, 3), converted)
        val nonTaxFlag = "NNNNNNNN"
        val nonTaxMap = mapOf("flags" to nonTaxFlag)
        val nonTaxConverted = sut.convert("", nonTaxMap, descriptor)
        assertEquals(BigDecimal.ZERO, nonTaxConverted)
    }

    @Test
    fun `test the UnitOfMeasure converter`() {
        val uomWeightFlag = "NNYNNNNN"
        val sut = UnitOfMeasure()
        val map = mapOf(storeFlags to uomWeightFlag)
        val converted = sut.convert("", map, descriptor)
        assertEquals(POUND, converted)
        val uomEachFlag = "NNNNNNN"
        val uomEachMap = mapOf("flags" to uomEachFlag)
        val uomEachConverted = sut.convert("", uomEachMap, descriptor)
        assertEquals(EACH, uomEachConverted)
    }
}
