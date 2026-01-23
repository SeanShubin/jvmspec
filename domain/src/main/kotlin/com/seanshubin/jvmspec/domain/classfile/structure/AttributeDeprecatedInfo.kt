package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class AttributeDeprecatedInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>
) : AttributeInfo {
    companion object {
        const val NAME = "Deprecated"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeDeprecatedInfo {
            // Zero-length attribute
            return AttributeDeprecatedInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info
            )
        }
    }
}
