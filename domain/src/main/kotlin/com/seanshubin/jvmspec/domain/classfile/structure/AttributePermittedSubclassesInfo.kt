package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributePermittedSubclassesInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val numberOfClasses: UShort,
    val classes: List<UShort>
) : AttributeInfo {
    companion object {
        const val NAME = "PermittedSubclasses"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributePermittedSubclassesInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val numberOfClasses = input.readUnsignedShort().toUShort()
            val classes = (0 until numberOfClasses.toInt()).map {
                input.readUnsignedShort().toUShort()
            }
            return AttributePermittedSubclassesInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                numberOfClasses,
                classes
            )
        }
    }
}
