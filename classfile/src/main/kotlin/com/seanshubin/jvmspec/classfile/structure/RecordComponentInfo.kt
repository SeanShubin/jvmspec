package com.seanshubin.jvmspec.classfile.structure

import java.io.DataInput

data class RecordComponentInfo(
    val nameIndex: UShort,
    val descriptorIndex: UShort,
    val attributesCount: UShort,
    val attributes: List<AttributeInfo>
) {
    companion object {
        fun fromDataInput(
            input: DataInput,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): RecordComponentInfo {
            val nameIndex = input.readUnsignedShort().toUShort()
            val descriptorIndex = input.readUnsignedShort().toUShort()
            val attributesCount = input.readUnsignedShort().toUShort()
            val attributes = (0 until attributesCount.toInt()).map {
                attributeInfoFromDataInput(input, constantPoolMap)
            }
            return RecordComponentInfo(nameIndex, descriptorIndex, attributesCount, attributes)
        }
    }
}
