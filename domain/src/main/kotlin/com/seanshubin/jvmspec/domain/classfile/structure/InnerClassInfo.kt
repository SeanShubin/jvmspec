package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class InnerClassInfo(
    val innerClassInfoIndex: UShort,
    val outerClassInfoIndex: UShort,
    val innerNameIndex: UShort,
    val innerClassAccessFlags: UShort
) {
    companion object {
        fun fromDataInput(input: DataInput): InnerClassInfo {
            val innerClassInfoIndex = input.readUnsignedShort().toUShort()
            val outerClassInfoIndex = input.readUnsignedShort().toUShort()
            val innerNameIndex = input.readUnsignedShort().toUShort()
            val innerClassAccessFlags = input.readUnsignedShort().toUShort()
            return InnerClassInfo(innerClassInfoIndex, outerClassInfoIndex, innerNameIndex, innerClassAccessFlags)
        }
    }
}
