package net.kamradt.pfm.data

import java.math.BigDecimal

data class StoreFileDescriptor(
    val name: String,
    val fields: List<StoreFileDescriptorField>
) {
    fun convertLong(name: String, storeData: Map<String, String>): Long =
        storeData.getValue(name).toLong()

    fun convertOptString(name: String, storeData: Map<String, String>): String? =
        if (!storeData.containsKey(name))
            null
        else
            storeData.getValue(name).trim();

    fun convertBigDecimal(name: String, storeData: Map<String, String>): BigDecimal =
        BigDecimal.valueOf(storeData.getValue(name).toLong(), 2)

    fun convertOptBigDecimal(name: String, storeData: Map<String, String>): BigDecimal? =
        if (!storeData.containsKey(name))
            null
        else
            BigDecimal.valueOf(storeData.getValue(name).toLong(), 2)
}

enum class FileFieldType {
    Number,
    String,
    Currency,
    Flag
}

data class StoreFileDescriptorField(
    val storeFileFieldName: String,
    val storeFileFieldType: FileFieldType,
    val productDescriptionField: String?,
    val productDescriptionType: String?,
    val startOffset: Int,
    val endOffset: Int,
    val converterClassName: String? = null
)
