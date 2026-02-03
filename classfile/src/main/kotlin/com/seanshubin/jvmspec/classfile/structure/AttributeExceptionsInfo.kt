package com.seanshubin.jvmspec.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeExceptionsInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val numberOfExceptions: UShort,
    val exceptionIndexTable: List<UShort>
) : AttributeInfo {
    companion object {
        const val NAME = "Exceptions"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeExceptionsInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val numberOfExceptions = input.readUnsignedShort().toUShort()
            val exceptionIndexTable = (0 until numberOfExceptions.toInt()).map {
                input.readUnsignedShort().toUShort()
            }
            return AttributeExceptionsInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                numberOfExceptions,
                exceptionIndexTable
            )
        }
    }
}
