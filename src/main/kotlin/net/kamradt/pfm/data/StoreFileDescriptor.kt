package net.kamradt.pfm.data

import net.kamradt.pfm.api.StoreFileDescriptorConverter
import java.math.BigDecimal

data class StoreFileDescriptor(
    val name: String,
    val fields: List<StoreFileDescriptorField>
) {
    fun convertLong(name: String, storeData: Map<String, String>): Long {
        TODO("Not yet implemented")
    }

    fun convertOptString(name: String, storeData: Map<String, String>): String? {
        TODO("Not yet implemented")
    }

    fun convertBigDecimal(name: String, storeData: Map<String, String>): BigDecimal {
        TODO("Not yet implemented")
    }

    fun convertOptBigDecimal(name: String, storeData: Map<String, String>): BigDecimal? {
        TODO("Not yet implemented")
    }
}

data class StoreFileDescriptorField(
    val storeFileFieldName: String,
    val productDescriptionField: String?,
    val startOffset: Int,
    val endOffset: Int,
    val converterClassName: String? = null
)
