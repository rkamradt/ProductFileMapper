package net.kamradt.pfm

import java.math.BigDecimal

data class ProductDescription(
    val productID: String,
    val productDescription: String? = null,
    val regularDisplayPrice: String? = null, // (English-readable string of your choosing, e.g. "$1.00" or "3 for $1.00")
    val regularCalculatorPrice: BigDecimal, // (price the calculator should use, rounded to 4 decimal places, half-down)
    val promotionalDisplayPrice: String? = null, // if it exists (same format as regular display price string)
    val promotionalCalculatorPrice: BigDecimal? = null,  // if it exists
    val unitOfMeasure: String? = null, //  Weighted items are per pound
    val productSize: String? = null,
    val taxRate: BigDecimal
)
