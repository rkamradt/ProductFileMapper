package net.kamradt.pfm.data

/**
 * ProductDescription constants
 */
const val EACH = "Each"
const val POUND = "Pound"
const val TAX_RATE = 7775L

/**
 * ProductDescription field names
 */
const val productIdField = "productId"
const val productDescriptionField = "productDescription"
const val regularDisplayPriceField = "regularDisplayPrice"
const val regularCalculatedPriceField = "regularCalculatedPrice"
const val promotionalDisplayPriceField = "promotionalDisplayPrice"
const val promotionalCalculatedPriceField = "promotionalCalculatedPrice"
const val unitOfMeasureField = "unitOfMeasure"
const val productSizeField = "productSize"
const val taxRateField = "taxRate"

/**
 * Store File constants
 */
const val TAX_FLAG_INDEX = 4
const val PER_WEIGH_INDEX = 2
const val MAX_ROW_SIZE = 142

/**
 * Store File fields
 */
const val storeProductId = "productId"
const val storeProductDescription = "productDescription"
const val storeRegularSingularPrice = "regularSingularPrice"
const val storePromotionalSingularPrice = "promotionalSingularPrice"
const val storeRegularSplitPrice = "regularSplitPrice"
const val storePromotionalSplitPrice = "promotionalSplitPrice"
const val storeRegularForX = "regularForX"
const val storePromotionalForX = "promotionalForX"
const val storeFlags = "flags"
const val storeProductSize = "productSize"
