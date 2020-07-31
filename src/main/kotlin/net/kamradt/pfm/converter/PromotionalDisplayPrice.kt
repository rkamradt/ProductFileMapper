package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor
import net.kamradt.pfm.data.storePromotionalForX
import net.kamradt.pfm.data.storePromotionalSingularPrice
import net.kamradt.pfm.data.storePromotionalSplitPrice
import java.math.BigDecimal

class PromotionalDisplayPrice : StoreFileDescriptorConverter<String?> {
    override fun convert(
        fieldName: String,
        data: Map<String, String>,
        descriptor: StoreFileDescriptor
    ): String? {
        val split = data[storePromotionalSplitPrice]?.toLong() ?: 0L != 0L
        val price = if (split)
            BigDecimal.valueOf(data[storePromotionalSplitPrice]?.toLong() ?: 0L, 2)
        else
            BigDecimal.valueOf(data[storePromotionalSingularPrice]?.toLong() ?: 0L, 2)
        return if (split)
            "${data[storePromotionalForX]?.toInt()} for \$$price"
        else
            "\$$price"
    }
}
