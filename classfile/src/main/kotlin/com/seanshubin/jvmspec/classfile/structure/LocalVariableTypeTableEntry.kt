package com.seanshubin.jvmspec.classfile.structure

import java.io.DataInput

data class LocalVariableTypeTableEntry(
    val startPc: UShort,
    val length: UShort,
    val nameIndex: UShort,
    val signatureIndex: UShort,
    val index: UShort
) {
    companion object {
        fun fromDataInput(input: DataInput): LocalVariableTypeTableEntry {
            val startPc = input.readUnsignedShort().toUShort()
            val length = input.readUnsignedShort().toUShort()
            val nameIndex = input.readUnsignedShort().toUShort()
            val signatureIndex = input.readUnsignedShort().toUShort()
            val index = input.readUnsignedShort().toUShort()
            return LocalVariableTypeTableEntry(startPc, length, nameIndex, signatureIndex, index)
        }
    }
}
