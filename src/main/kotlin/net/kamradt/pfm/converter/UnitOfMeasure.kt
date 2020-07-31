package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor

class UnitOfMeasure : StoreFileDescriptorConverter<String?> {
    override fun convert(fieldName: String,
                         data: Map<String, String>,
                         descriptor: StoreFileDescriptor
    ): String? =
        if(data["flags"]?.get(4)?.equals('Y',true)!!)
            "Pound"
        else
            "Each"
}