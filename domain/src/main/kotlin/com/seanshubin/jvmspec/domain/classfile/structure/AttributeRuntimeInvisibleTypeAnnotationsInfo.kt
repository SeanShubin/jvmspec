package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeRuntimeInvisibleTypeAnnotationsInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val numAnnotations: UShort,
    val annotations: List<TypeAnnotation>
) : AttributeInfo {
    companion object {
        const val NAME = "RuntimeInvisibleTypeAnnotations"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeRuntimeInvisibleTypeAnnotationsInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val numAnnotations = input.readUnsignedShort().toUShort()
            val annotations = (0 until numAnnotations.toInt()).map {
                TypeAnnotation.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
            }
            return AttributeRuntimeInvisibleTypeAnnotationsInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                numAnnotations,
                annotations
            )
        }
    }
}
