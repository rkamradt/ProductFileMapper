package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor
import java.math.BigDecimal

class TaxRate : StoreFileDescriptorConverter<BigDecimal> {
    override fun convert(fieldName: String,
                         data: Map<String, String>,
                         descriptor: StoreFileDescriptor
    ): BigDecimal =
        if(data["flags"]?.get(2)?.equals('Y',true)!!)
            BigDecimal.valueOf(7775,3)
        else
            BigDecimal.ZERO
}