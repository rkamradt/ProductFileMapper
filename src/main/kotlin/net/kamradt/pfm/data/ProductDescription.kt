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
package net.kamradt.pfm.data

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import java.math.BigDecimal

/**
 * A ProductDescription for the system. It is expected in a real scenario this
 * will be defined elsewhere. The secondary constructor will create a ProductDescription
 * with the maps of converters and data and might be an extention function if this class
 * should come from somewhere else
 */
data class ProductDescription(
    val productId: Long,
    val productDescription: String? = null,
    val regularDisplayPrice: String? = null, // (English-readable string of your choosing, e.g. "$1.00" or "3 for $1.00")
    val regularCalculatorPrice: BigDecimal, // (price the calculator should use, rounded to 4 decimal places, half-down)
    val promotionalDisplayPrice: String? = null, // if it exists (same format as regular display price string)
    val promotionalCalculatorPrice: BigDecimal? = null, // if it exists
    val unitOfMeasure: String? = null, //  Weighted items are per pound
    val productSize: String? = null,
    val taxRate: BigDecimal
) {
    constructor(
        converters: Map<String, StoreFileDescriptorConverter<Any>>,
        data: Map<String, String>,
        descriptor: StoreFileDescriptor
    ) : this(
        productId = converters[productIdField]?.convert(storeProductId, data, descriptor) as Long,
        productDescription = converters[productDescriptionField]?.convert(storeProductDescription, data, descriptor) as String?,
        regularDisplayPrice = converters[regularDisplayPriceField]?.convert("", data, descriptor) as String?,
        regularCalculatorPrice = converters[regularCalculatedPriceField]?.convert("", data, descriptor) as BigDecimal,
        promotionalDisplayPrice = converters[promotionalDisplayPriceField]?.convert("", data, descriptor) as String?,
        promotionalCalculatorPrice = converters[promotionalCalculatedPriceField]?.convert("", data, descriptor) as BigDecimal?,
        unitOfMeasure = converters[unitOfMeasureField]?.convert("", data, descriptor) as String?,
        productSize = converters[productSizeField]?.convert(storeProductSize, data, descriptor) as String?,
        taxRate = converters[taxRateField]?.convert("", data, descriptor) as BigDecimal
    )
}
