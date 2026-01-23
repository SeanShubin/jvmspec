package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeEnclosingMethodInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val classIndex: UShort,
    val methodIndex: UShort
) : AttributeInfo {
    companion object {
        const val NAME = "EnclosingMethod"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeEnclosingMethodInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val classIndex = input.readUnsignedShort().toUShort()
            val methodIndex = input.readUnsignedShort().toUShort()
            return AttributeEnclosingMethodInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                classIndex,
                methodIndex
            )
        }
    }
}
