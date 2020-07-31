package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor
import net.kamradt.pfm.data.storeRegularForX
import net.kamradt.pfm.data.storeRegularSingularPrice
import net.kamradt.pfm.data.storeRegularSplitPrice
import java.math.BigDecimal
import java.math.RoundingMode

class RegularCalculatedPrice : StoreFileDescriptorConverter<BigDecimal> {
    override fun convert(
        fieldName: String,
        data: Map<String, String>,
        descriptor: StoreFileDescriptor
    ): BigDecimal {
        val split = data[storeRegularSplitPrice]?.toLong() ?: 0L != 0L
        val price = if (split)
            BigDecimal.valueOf(data[storeRegularSplitPrice]?.toLong() ?: 0L, 2).setScale(4)
        else
            BigDecimal.valueOf(data[storeRegularSingularPrice]?.toLong() ?: 0L, 2).setScale(4)
        val forX = data[storeRegularForX]?.toLong() ?: 0L
        return if (split)
            if (forX == 0L)
                BigDecimal.ZERO
            else
                price.divide(BigDecimal.valueOf(forX), RoundingMode.HALF_DOWN)
        else
            price
    }
}
