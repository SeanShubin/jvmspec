package com.seanshubin.jvmspec.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeRecordInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val componentsCount: UShort,
    val components: List<RecordComponentInfo>
) : AttributeInfo {
    companion object {
        const val NAME = "Record"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeRecordInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val componentsCount = input.readUnsignedShort().toUShort()
            val components = (0 until componentsCount.toInt()).map {
                RecordComponentInfo.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
            }
            return AttributeRecordInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                componentsCount,
                components
            )
        }
    }
}
