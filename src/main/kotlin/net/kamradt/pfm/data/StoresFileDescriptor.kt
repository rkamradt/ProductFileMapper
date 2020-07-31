package net.kamradt.pfm.data

data class StoresFileDescriptor(
    val type: String, // should always be Stores in yaml
    val stores: List<StoreFileDescriptor>
)

data class StoreFileDescriptor(
    val name: String,
    val fields: List<StoreFileDescriptorField>
)

data class StoreFileDescriptorField(
    val storeFileFieldName: String? = null,
    val productDescriptionField: String? = null,
    val startOffset: Int? = null,
    val endOffset: Int? = null,
    val converterClassName: String? = null
)
