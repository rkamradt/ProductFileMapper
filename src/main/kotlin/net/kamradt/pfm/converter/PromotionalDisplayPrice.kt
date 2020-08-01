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

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor
import net.kamradt.pfm.data.storePromotionalForX
import net.kamradt.pfm.data.storePromotionalSingularPrice
import net.kamradt.pfm.data.storePromotionalSplitPrice
import java.math.BigDecimal

/**
 * A complex converter that will look for 'promotionalSplitPrice' or
 * 'promotionalSingularPrice' If it finds the 'promotionalSplitPrice' it will
 * return a string "<n> for $<price>". If not
 * it will return "$<price>". Will throw a NumberFormatException
 * if any of the numbers are invalid
 *
 */
class PromotionalDisplayPrice : StoreFileDescriptorConverter<String?> {
    override fun convert(
        fieldName: String,
        data: Map<String, String>,
        descriptor: StoreFileDescriptor
    ): String? {
        val split = data[storePromotionalSplitPrice]?.toLong() ?: 0L != 0L
        val price = if (split)
            BigDecimal.valueOf(data[storePromotionalSplitPrice]?.toLong() ?: 0L, 2)
        else
            BigDecimal.valueOf(data[storePromotionalSingularPrice]?.toLong() ?: 0L, 2)
        return if (split)
            "${data[storePromotionalForX]?.toInt()} for \$$price"
        else
            "\$$price"
    }
}
