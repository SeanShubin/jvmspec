package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class ModuleExports(
    val exportsIndex: UShort,
    val exportsFlags: Int,
    val exportsToCount: UShort,
    val exportsToIndex: List<UShort>
) {
    companion object {
        fun fromDataInput(input: DataInput): ModuleExports {
            val exportsIndex = input.readUnsignedShort().toUShort()
            val exportsFlags = input.readUnsignedShort()
            val exportsToCount = input.readUnsignedShort().toUShort()
            val exportsToIndex = (0 until exportsToCount.toInt()).map {
                input.readUnsignedShort().toUShort()
            }
            return ModuleExports(exportsIndex, exportsFlags, exportsToCount, exportsToIndex)
        }
    }
}
