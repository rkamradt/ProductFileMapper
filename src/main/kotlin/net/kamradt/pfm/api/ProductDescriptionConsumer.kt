package net.kamradt.pfm.api

import net.kamradt.pfm.data.ProductDescription


interface ProductDescriptionConsumer {
    suspend fun action(value: ProductDescription)
}
