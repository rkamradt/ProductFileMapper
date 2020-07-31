package net.kamradt.pfm.data

import java.math.BigDecimal

data class StoresFileDescriptor(
    val type: String, // should always be Stores in yaml
    val stores: List<StoreFileDescriptor>
)

data class StoreFileDescriptor(
    val name: String,
    val fields: List<StoreFileDescriptorField>
)

enum class FileFieldType {
    Number,
    String,
    Currency,
    Flag
}

data class StoreFileDescriptorField(
    val storeFileFieldName: String? = null,
    val storeFileFieldType: FileFieldType? = null,
    val productDescriptionField: String? = null,
    val productDescriptionType: String? = null,
    val startOffset: Int? = null,
    val endOffset: Int? = null,
    val converterClassName: String? = null
)

fun convertLong(name: String, storeData: Map<String, String>, descriptor: StoreFileDescriptor): Long =
    if (!storeData.containsKey(name))
        0L
    else
        storeData.getValue(name).toLong()

fun convertOptString(name: String, storeData: Map<String, String>, descriptor: StoreFileDescriptor): String? =
    if (!storeData.containsKey(name))
        null
    else
        storeData.getValue(name).trim();

fun convertBigDecimal(name: String, storeData: Map<String, String>, descriptor: StoreFileDescriptor): BigDecimal =
    if (!storeData.containsKey(name))
        BigDecimal.ZERO
    else
        BigDecimal.valueOf(storeData.getValue(name).toLong(), 2)

fun convertOptBigDecimal(name: String, storeData: Map<String, String>, descriptor: StoreFileDescriptor): BigDecimal? =
    if (!storeData.containsKey(name))
        null
    else
        BigDecimal.valueOf(storeData.getValue(name).toLong(), 2)
