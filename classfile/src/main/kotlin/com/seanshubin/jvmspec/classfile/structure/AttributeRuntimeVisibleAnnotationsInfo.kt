package com.seanshubin.jvmspec.classfile.structure

import com.seanshubin.jvmspec.classfile.structure.AnnotationStructure.Annotation
import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeRuntimeVisibleAnnotationsInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val numAnnotations: UShort,
    val annotations: List<Annotation>
) : AttributeInfo {
    companion object {
        const val NAME = "RuntimeVisibleAnnotations"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeRuntimeVisibleAnnotationsInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val numAnnotations = input.readUnsignedShort().toUShort()
            val annotations = (0 until numAnnotations.toInt()).map {
                Annotation.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
            }
            return AttributeRuntimeVisibleAnnotationsInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                numAnnotations,
                annotations
            )
        }
    }
}
