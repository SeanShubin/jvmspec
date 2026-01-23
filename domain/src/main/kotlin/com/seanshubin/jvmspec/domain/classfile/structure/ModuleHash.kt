package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class ModuleHash(
    val moduleNameIndex: UShort,
    val hashLength: UShort,
    val hash: List<Byte>
) {
    companion object {
        fun fromDataInput(input: DataInput): ModuleHash {
            val moduleNameIndex = input.readUnsignedShort().toUShort()
            val hashLength = input.readUnsignedShort().toUShort()
            val hash = (0 until hashLength.toInt()).map {
                input.readByte()
            }
            return ModuleHash(moduleNameIndex, hashLength, hash)
        }
    }
}
