package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

data class ModuleOpens(
    val opensIndex: UShort,
    val opensFlags: Int,
    val opensToCount: UShort,
    val opensToIndex: List<UShort>
) {
    companion object {
        fun fromDataInput(input: DataInput): ModuleOpens {
            val opensIndex = input.readUnsignedShort().toUShort()
            val opensFlags = input.readUnsignedShort()
            val opensToCount = input.readUnsignedShort().toUShort()
            val opensToIndex = (0 until opensToCount.toInt()).map {
                input.readUnsignedShort().toUShort()
            }
            return ModuleOpens(opensIndex, opensFlags, opensToCount, opensToIndex)
        }
    }
}
