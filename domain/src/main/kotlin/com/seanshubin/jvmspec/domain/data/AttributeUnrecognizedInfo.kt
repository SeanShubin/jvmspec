package com.seanshubin.jvmspec.domain.data

data class AttributeUnrecognizedInfo(
    override val attributeNameIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>
) : AttributeInfo {
    companion object {
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolLookup: ConstantPoolLookup
        ): AttributeUnrecognizedInfo {
            return AttributeUnrecognizedInfo(
                attributeInfo.attributeNameIndex,
                attributeInfo.attributeLength,
                attributeInfo.info
            )
        }
    }
}