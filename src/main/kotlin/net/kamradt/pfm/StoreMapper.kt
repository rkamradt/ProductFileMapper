package net.kamradt.pfm

interface StoreMapper {
    fun mapFromStore(row: String): ProductDescription?

}
