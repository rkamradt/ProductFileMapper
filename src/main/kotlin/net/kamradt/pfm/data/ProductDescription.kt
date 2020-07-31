package net.kamradt.pfm.data

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import java.math.BigDecimal

data class ProductDescription(
    val productId: Long,
    val productDescription: String? = null,
    val regularDisplayPrice: String? = null, // (English-readable string of your choosing, e.g. "$1.00" or "3 for $1.00")
    val regularCalculatorPrice: BigDecimal, // (price the calculator should use, rounded to 4 decimal places, half-down)
    val promotionalDisplayPrice: String? = null, // if it exists (same format as regular display price string)
    val promotionalCalculatorPrice: BigDecimal? = null,  // if it exists
    val unitOfMeasure: String? = null, //  Weighted items are per pound
    val productSize: String? = null,
    val taxRate: BigDecimal
) {
    constructor(converters: Map<String, StoreFileDescriptorConverter<Any>>,
                data: Map<String, String>,
                descriptor: StoreFileDescriptor
    ) : this(
        converters["productId"]?.convert("productId", data, descriptor) as Long,
        converters["productDescription"]?.convert("productDescription", data, descriptor) as String?,
        converters["regularDisplayPrice"]?.convert("regularDisplayPrice", data, descriptor) as String?,
        converters["regularCalculatorPrice"]?.convert("regularCalculatorPrice", data, descriptor) as BigDecimal,
        converters["promotionalDisplayPrice"]?.convert("promotionalDisplayPrice", data, descriptor) as String?,
        converters["promotionalCalculatorPrice"]?.convert("promotionalCalculatorPrice", data, descriptor) as BigDecimal?,
        converters["unitOfMeasure"]?.convert("unitOfMeasure", data, descriptor) as String?,
        converters["productSize"]?.convert("productSize", data, descriptor) as String?,
        converters["taxRate"]?.convert("taxRate", data, descriptor) as BigDecimal
    )
}
