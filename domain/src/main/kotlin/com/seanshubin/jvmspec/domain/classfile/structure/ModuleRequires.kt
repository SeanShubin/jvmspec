package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class ModuleRequires(
    val requiresIndex: UShort,
    val requiresFlags: Int,
    val requiresVersionIndex: UShort
) {
    companion object {
        fun fromDataInput(input: DataInput): ModuleRequires {
            val requiresIndex = input.readUnsignedShort().toUShort()
            val requiresFlags = input.readUnsignedShort()
            val requiresVersionIndex = input.readUnsignedShort().toUShort()
            return ModuleRequires(requiresIndex, requiresFlags, requiresVersionIndex)
        }
    }
}
