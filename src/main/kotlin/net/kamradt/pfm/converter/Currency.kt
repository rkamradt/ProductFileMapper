package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor
import java.math.BigDecimal
import java.math.RoundingMode

class Currency : StoreFileDescriptorConverter<BigDecimal> {
    override fun convert(fieldName: String,
                         data: Map<String, String>,
                         descriptor: StoreFileDescriptor
    ): BigDecimal =
        BigDecimal.valueOf(data[fieldName]?.toLong() ?: 0L,2).setScale(4,RoundingMode.HALF_DOWN)
}