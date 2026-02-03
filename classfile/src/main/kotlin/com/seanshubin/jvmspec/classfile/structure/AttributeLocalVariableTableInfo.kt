package com.seanshubin.jvmspec.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeLocalVariableTableInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val localVariableTableLength: UShort,
    val localVariableTable: List<LocalVariableTableEntry>
) : AttributeInfo {
    companion object {
        const val NAME = "LocalVariableTable"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeLocalVariableTableInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val localVariableTableLength = input.readUnsignedShort().toUShort()
            val localVariableTable = (0 until localVariableTableLength.toInt()).map {
                LocalVariableTableEntry.fromDataInput(input)
            }
            return AttributeLocalVariableTableInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                localVariableTableLength,
                localVariableTable
            )
        }
    }
}
