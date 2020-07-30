package net.kamradt.pfm

import net.kamradt.pfm.api.ProductDescriptionConsumer
import net.kamradt.pfm.data.ProductDescription

class MapProductDescriptionConsumer : ProductDescriptionConsumer {
    val map = mutableMapOf<Long, ProductDescription>()
    override suspend fun action(value: ProductDescription) {
        map.put(value.productId, value)
    }
}