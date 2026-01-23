package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.structure.AnnotationStructure.Annotation
import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeRuntimeInvisibleAnnotationsInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val numAnnotations: UShort,
    val annotations: List<Annotation>
) : AttributeInfo {
    companion object {
        const val NAME = "RuntimeInvisibleAnnotations"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeRuntimeInvisibleAnnotationsInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val numAnnotations = input.readUnsignedShort().toUShort()
            val annotations = (0 until numAnnotations.toInt()).map {
                Annotation.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
            }
            return AttributeRuntimeInvisibleAnnotationsInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                numAnnotations,
                annotations
            )
        }
    }
}
