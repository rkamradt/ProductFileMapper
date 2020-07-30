package net.kamradt.pfm.api

import net.kamradt.pfm.data.ProductDescription

interface StoreMapper {
    fun mapFromStore(row: String): Map<String, String>?
    fun mapToProduct(storeData: Map<String, String>): ProductDescription?

}
