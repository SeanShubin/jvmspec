package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantFloatInfo(
    override val tag: Byte,
    val bytes: Int
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 4

        fun fromDataInput(tag: Byte, input: DataInput): ConstantFloatInfo {
            val bytes = input.readInt()
            return ConstantFloatInfo(tag, bytes)
        }
    }
}
