package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.*

class UnitOfMeasure : StoreFileDescriptorConverter<String?> {
    override fun convert(fieldName: String,
                         data: Map<String, String>,
                         descriptor: StoreFileDescriptor
    ): String? =
        if(data[storeFlags]?.padEnd(8)?.get(PER_WEIGH_INDEX)?.equals('Y',true)!!)
            POUND
        else
            EACH
}