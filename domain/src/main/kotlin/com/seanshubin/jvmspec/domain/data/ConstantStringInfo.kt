package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantStringInfo(
    override val tag: Byte,
    val stringIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 8

        fun fromDataInput(tag: Byte, input: DataInput): ConstantStringInfo {
            val stringIndex = input.readShort()
            return ConstantStringInfo(tag, stringIndex)
        }
    }
}