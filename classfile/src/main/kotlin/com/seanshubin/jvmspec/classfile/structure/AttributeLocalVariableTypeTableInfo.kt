package com.seanshubin.jvmspec.classfile.structure

import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeLocalVariableTypeTableInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val localVariableTypeTableLength: UShort,
    val localVariableTypeTable: List<LocalVariableTypeTableEntry>
) : AttributeInfo {
    companion object {
        const val NAME = "LocalVariableTypeTable"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeLocalVariableTypeTableInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val localVariableTypeTableLength = input.readUnsignedShort().toUShort()
            val localVariableTypeTable = (0 until localVariableTypeTableLength.toInt()).map {
                LocalVariableTypeTableEntry.fromDataInput(input)
            }
            return AttributeLocalVariableTypeTableInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                localVariableTypeTableLength,
                localVariableTypeTable
            )
        }
    }
}
