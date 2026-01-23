package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class LineNumberTableEntry(
    val startPc: UShort,
    val lineNumber: UShort
) {
    companion object {
        fun fromDataInput(input: DataInput): LineNumberTableEntry {
            val startPc = input.readUnsignedShort().toUShort()
            val lineNumber = input.readUnsignedShort().toUShort()
            return LineNumberTableEntry(startPc, lineNumber)
        }
    }
}
