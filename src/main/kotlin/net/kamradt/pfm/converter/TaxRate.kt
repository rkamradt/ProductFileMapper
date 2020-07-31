package net.kamradt.pfm.converter

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import net.kamradt.pfm.data.StoreFileDescriptor
import net.kamradt.pfm.data.TAX_FLAG_INDEX
import net.kamradt.pfm.data.TAX_RATE
import net.kamradt.pfm.data.storeFlags
import java.math.BigDecimal

class TaxRate : StoreFileDescriptorConverter<BigDecimal> {
    override fun convert(
        fieldName: String,
        data: Map<String, String>,
        descriptor: StoreFileDescriptor
    ): BigDecimal =
        if (data[storeFlags]?.padEnd(8)?.get(TAX_FLAG_INDEX)?.equals('Y', true)!!)
            BigDecimal.valueOf(TAX_RATE, 3)
        else
            BigDecimal.ZERO
}
