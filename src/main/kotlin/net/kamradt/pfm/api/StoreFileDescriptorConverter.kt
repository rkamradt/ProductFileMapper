package net.kamradt.pfm.api

interface StoreFileDescriptorConverter<I, O> {
    fun convert(input: I): O?
}

