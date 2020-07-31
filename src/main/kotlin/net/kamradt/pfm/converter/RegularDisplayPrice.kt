package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor
import java.math.BigDecimal

class RegularDisplayPrice : StoreFileDescriptorConverter<String?> {
    override fun convert(fieldName: String,
                         data: Map<String, String>,
                         descriptor: StoreFileDescriptor
    ): String? {
        val price = data["promotionalSplitPrice"]?.toLong()?.let {
            BigDecimal.valueOf(it, 2)
        }
        return "\$${price?:0}"
    }
}