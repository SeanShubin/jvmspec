package com.seanshubin.jvmspec.classfile.structure

import java.io.DataInput

data class LocalVariableTableEntry(
    val startPc: UShort,
    val length: UShort,
    val nameIndex: UShort,
    val descriptorIndex: UShort,
    val index: UShort
) {
    companion object {
        fun fromDataInput(input: DataInput): LocalVariableTableEntry {
            val startPc = input.readUnsignedShort().toUShort()
            val length = input.readUnsignedShort().toUShort()
            val nameIndex = input.readUnsignedShort().toUShort()
            val descriptorIndex = input.readUnsignedShort().toUShort()
            val index = input.readUnsignedShort().toUShort()
            return LocalVariableTableEntry(startPc, length, nameIndex, descriptorIndex, index)
        }
    }
}
