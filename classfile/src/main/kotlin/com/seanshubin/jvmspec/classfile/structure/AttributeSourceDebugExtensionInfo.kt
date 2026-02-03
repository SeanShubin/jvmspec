package com.seanshubin.jvmspec.classfile.structure

import java.io.DataInput

data class AttributeSourceDebugExtensionInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>
) : AttributeInfo {
    companion object {
        const val NAME = "SourceDebugExtension"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeSourceDebugExtensionInfo {
            // The info bytes contain UTF-8 debug data directly
            return AttributeSourceDebugExtensionInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info
            )
        }
    }
}
