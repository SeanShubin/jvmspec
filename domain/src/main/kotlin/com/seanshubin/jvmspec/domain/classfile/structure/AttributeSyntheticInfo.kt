package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class AttributeSyntheticInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>
) : AttributeInfo {
    companion object {
        const val NAME = "Synthetic"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeSyntheticInfo {
            // Zero-length attribute
            return AttributeSyntheticInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info
            )
        }
    }
}
