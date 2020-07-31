package net.kamradt.pfm.api

import net.kamradt.pfm.data.StoreFileDescriptor

interface StoreFileDescriptorConverter<out O> {
    fun convert(
        fieldName: String,
        data: Map<String, String>,
        descriptor: StoreFileDescriptor
    ): O
}
