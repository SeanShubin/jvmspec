package com.seanshubin.jvmspec.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeRuntimeVisibleParameterAnnotationsInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val numParameters: UByte,
    val parameterAnnotations: List<ParameterAnnotation>
) : AttributeInfo {
    companion object {
        const val NAME = "RuntimeVisibleParameterAnnotations"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeRuntimeVisibleParameterAnnotationsInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val numParameters = input.readUnsignedByte().toUByte()
            val parameterAnnotations = (0 until numParameters.toInt()).map {
                ParameterAnnotation.fromDataInput(input, constantPoolMap, attributeInfoFromDataInput)
            }
            return AttributeRuntimeVisibleParameterAnnotationsInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                numParameters,
                parameterAnnotations
            )
        }
    }
}
