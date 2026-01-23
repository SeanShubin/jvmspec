package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class MethodParameter(
    val nameIndex: UShort,
    val accessFlags: UShort
) {
    companion object {
        fun fromDataInput(input: DataInput): MethodParameter {
            val nameIndex = input.readUnsignedShort().toUShort()
            val accessFlags = input.readUnsignedShort().toUShort()
            return MethodParameter(nameIndex, accessFlags)
        }
    }
}
