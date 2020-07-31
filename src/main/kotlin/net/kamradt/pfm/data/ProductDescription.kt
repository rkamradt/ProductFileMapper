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
