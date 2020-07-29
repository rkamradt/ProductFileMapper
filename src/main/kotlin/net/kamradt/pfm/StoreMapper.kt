package net.kamradt.pfm

interface StoreMapper {
    fun mapFromStore(row: String): Map<String, String>?
    fun mapToProduct(storeData: Map<String, String>): ProductDescription?

}
