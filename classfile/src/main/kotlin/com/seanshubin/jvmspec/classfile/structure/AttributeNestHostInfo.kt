package com.seanshubin.jvmspec.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeNestHostInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val hostClassIndex: UShort
) : AttributeInfo {
    companion object {
        const val NAME = "NestHost"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeNestHostInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val hostClassIndex = input.readUnsignedShort().toUShort()
            return AttributeNestHostInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                hostClassIndex
            )
        }
    }
}
