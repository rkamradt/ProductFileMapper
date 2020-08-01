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
