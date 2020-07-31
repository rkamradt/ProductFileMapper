package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.EACH
import net.kamradt.pfm.data.PER_WEIGH_INDEX
import net.kamradt.pfm.data.POUND
import net.kamradt.pfm.data.StoreFileDescriptor
import net.kamradt.pfm.data.storeFlags

class UnitOfMeasure : StoreFileDescriptorConverter<String?> {
    override fun convert(
        fieldName: String,
        data: Map<String, String>,
        descriptor: StoreFileDescriptor
    ): String? =
        if (data[storeFlags]?.padEnd(8)?.get(PER_WEIGH_INDEX)?.equals('Y', true)!!)
            POUND
        else
            EACH
}
