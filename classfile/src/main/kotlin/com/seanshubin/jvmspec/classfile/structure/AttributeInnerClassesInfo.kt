package com.seanshubin.jvmspec.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeInnerClassesInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val numberOfClasses: UShort,
    val classes: List<InnerClassInfo>
) : AttributeInfo {
    companion object {
        const val NAME = "InnerClasses"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeInnerClassesInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val numberOfClasses = input.readUnsignedShort().toUShort()
            val classes = (0 until numberOfClasses.toInt()).map {
                InnerClassInfo.fromDataInput(input)
            }
            return AttributeInnerClassesInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                numberOfClasses,
                classes
            )
        }
    }
}
