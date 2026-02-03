package com.seanshubin.jvmspec.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeMethodParametersInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val parametersCount: UByte,
    val parameters: List<MethodParameter>
) : AttributeInfo {
    companion object {
        const val NAME = "MethodParameters"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeMethodParametersInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val parametersCount = input.readUnsignedByte().toUByte()
            val parameters = (0 until parametersCount.toInt()).map {
                MethodParameter.fromDataInput(input)
            }
            return AttributeMethodParametersInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                parametersCount,
                parameters
            )
        }
    }
}
