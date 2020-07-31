package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor
import java.math.BigDecimal
import java.math.RoundingMode

class OptionalCurrency : StoreFileDescriptorConverter<BigDecimal?> {
    override fun convert(fieldName: String,
                         data: Map<String, String>,
                         descriptor: StoreFileDescriptor
    ): BigDecimal? =
        data[fieldName]?.toLong()?.let {
            BigDecimal.valueOf(it, 2).setScale(4, RoundingMode.HALF_DOWN)
        }


}