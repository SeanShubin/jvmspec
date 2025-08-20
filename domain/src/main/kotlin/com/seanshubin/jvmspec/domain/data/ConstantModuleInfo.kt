package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantModuleInfo(
    override val tag: Byte,
    val nameIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 19

        fun fromDataInput(tag: Byte, input: DataInput): ConstantModuleInfo {
            val nameIndex = input.readShort()
            return ConstantModuleInfo(tag, nameIndex)
        }
    }
}