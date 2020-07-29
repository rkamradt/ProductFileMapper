package net.kamradt.pfm

import java.math.BigDecimal

class SimpleStoreMapper : StoreMapper {
    val productIdString = "productId"
    val productNameString = "productName"
    val normalTaxRate = BigDecimal.valueOf(777,2)
    override fun mapFromStore(row: String): Map<String, String>? {
        val data = row.split(" ")
        if(data.size != 2) null
        val productId = data[1]
        val productName = data[0]
        return mapOf(productIdString to productId, productNameString to productName)
    }

    override fun mapToProduct(storeData: Map<String, String>): ProductDescription? {
        return try {
            ProductDescription(
                productId = storeData.getValue(productIdString).toLong(),
                productDescription = storeData.getValue(productNameString),
                regularCalculatorPrice = BigDecimal.ONE,
                taxRate = normalTaxRate
            )
        } catch (ex: Exception) {
            null;
        }
    }

}