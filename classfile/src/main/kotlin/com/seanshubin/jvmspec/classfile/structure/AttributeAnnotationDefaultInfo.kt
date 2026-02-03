package com.seanshubin.jvmspec.classfile.structure

import com.seanshubin.jvmspec.classfile.structure.AnnotationStructure.ElementValue
import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeAnnotationDefaultInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val defaultValue: ElementValue
) : AttributeInfo {
    companion object {
        const val NAME = "AnnotationDefault"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeAnnotationDefaultInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val defaultValue = ElementValue.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
            return AttributeAnnotationDefaultInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                defaultValue
            )
        }
    }
}
