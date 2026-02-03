package com.seanshubin.jvmspec.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeNestMembersInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val numberOfClasses: UShort,
    val classes: List<UShort>
) : AttributeInfo {
    companion object {
        const val NAME = "NestMembers"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeNestMembersInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val numberOfClasses = input.readUnsignedShort().toUShort()
            val classes = (0 until numberOfClasses.toInt()).map {
                input.readUnsignedShort().toUShort()
            }
            return AttributeNestMembersInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                numberOfClasses,
                classes
            )
        }
    }
}
