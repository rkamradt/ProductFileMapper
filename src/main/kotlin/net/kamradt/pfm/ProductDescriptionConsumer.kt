package net.kamradt.pfm


interface ProductDescriptionConsumer {
    suspend fun action(value: ProductDescription)
}
