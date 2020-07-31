package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor
import java.math.BigDecimal

class PromotionalDisplayPrice : StoreFileDescriptorConverter<String?> {
    override fun convert(fieldName: String,
                         data: Map<String, String>,
                         descriptor: StoreFileDescriptor
    ): String? {
        val price = data["promotionalSplitPrice"]?.toLong()?.let {
            BigDecimal.valueOf(it, 2)
        }
        val x = data["promotionalForX"]
        return "${x} for \$${price?:0}"
    }

}