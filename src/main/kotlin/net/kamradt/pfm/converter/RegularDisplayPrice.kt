package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor
import net.kamradt.pfm.data.storeRegularForX
import net.kamradt.pfm.data.storeRegularSingularPrice
import net.kamradt.pfm.data.storeRegularSplitPrice
import java.math.BigDecimal

class RegularDisplayPrice : StoreFileDescriptorConverter<String?> {
    override fun convert(
        fieldName: String,
        data: Map<String, String>,
        descriptor: StoreFileDescriptor
    ): String? {
        val split = data[storeRegularSplitPrice]?.toLong() ?: 0L != 0L
        val price = if (split)
            BigDecimal.valueOf(data[storeRegularSplitPrice]?.toLong() ?: 0L, 2)
        else
            BigDecimal.valueOf(data[storeRegularSingularPrice]?.toLong() ?: 0L, 2)
        return if (split)
            "${data[storeRegularForX]?.toInt()} for \$$price"
        else
            "\$$price"
    }
}
