package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor
import net.kamradt.pfm.data.storePromotionalForX
import net.kamradt.pfm.data.storePromotionalSingularPrice
import net.kamradt.pfm.data.storePromotionalSplitPrice
import java.math.BigDecimal
import java.math.RoundingMode

class PromotionalCalculatedPrice : StoreFileDescriptorConverter<BigDecimal?> {
    override fun convert(
        fieldName: String,
        data: Map<String, String>,
        descriptor: StoreFileDescriptor
    ): BigDecimal? {
        val split = data[storePromotionalSplitPrice]?.toLong() ?: 0L != 0L
        val price = if (split)
            BigDecimal.valueOf(data[storePromotionalSplitPrice]?.toLong() ?: 0L, 2).setScale(4)
        else
            BigDecimal.valueOf(data[storePromotionalSingularPrice]?.toLong() ?: 0L, 2).setScale(4)
        val forX = data[storePromotionalForX]?.toLong() ?: 0L
        return if (split)
            if (forX == 0L)
                BigDecimal.ZERO
            else
                price.divide(BigDecimal.valueOf(forX), RoundingMode.HALF_DOWN)
        else
            price
    }
}
