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
package net.kamradt.pfm.api

import net.kamradt.pfm.data.ProductDescription

/**
 * a two-phase mapper. The first phase 'mapFromStore' reads fix-
 * length records from a flat file and addes them to a map. The
 * second phase takes the data in the map and uses it to create
 * a ProductDescriptor
 */
interface StoreMapper {
    /**
     * take a row of fixed length text fields and create a map.
     * returns null if the row is invalid
     */
    fun mapFromStore(row: String): Map<String, String>?

    /**
     * take a map and create a ProductDescription. Returns null
     * if the map contains unparsable or invalid data
     */
    fun mapToProduct(storeData: Map<String, String>): ProductDescription?
}
