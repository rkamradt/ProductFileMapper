package net.kamradt.pfm

class MapProductDescriptionConsumer : ProductDescriptionConsumer {
    val map = mutableMapOf<Long, ProductDescription>()
    override suspend fun action(value: ProductDescription) {
        map.put(value.productId, value)
    }
}