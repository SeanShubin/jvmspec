package com.seanshubin.jvmspec.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeBootstrapMethodsInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val numBootstrapMethods: UShort,
    val bootstrapMethods: List<BootstrapMethod>
) : AttributeInfo {
    companion object {
        const val NAME = "BootstrapMethods"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeBootstrapMethodsInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val numBootstrapMethods = input.readUnsignedShort().toUShort()
            val bootstrapMethods = (0 until numBootstrapMethods.toInt()).map {
                BootstrapMethod.fromDataInput(input)
            }
            return AttributeBootstrapMethodsInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                numBootstrapMethods,
                bootstrapMethods
            )
        }
    }
}
