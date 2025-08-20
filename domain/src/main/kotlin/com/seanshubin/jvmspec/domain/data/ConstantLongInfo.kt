package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantLongInfo(
    override val tag: Byte,
    val highBytes: Int,
    val lowBytes: Int
) : ConstantInfo {
    override val entriesTaken: Int get() = 2

    companion object {
        const val TAG: Byte = 5

        fun fromDataInput(tag: Byte, input: DataInput): ConstantLongInfo {
            val highBytes = input.readInt()
            val lowBytes = input.readInt()
            return ConstantLongInfo(tag, highBytes, lowBytes)
        }
    }
}
