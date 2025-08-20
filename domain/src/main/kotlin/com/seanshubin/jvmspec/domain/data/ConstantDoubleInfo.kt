package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantDoubleInfo(
    override val tag: Byte,
    val highBytes: Int,
    val lowBytes: Int
) : ConstantInfo {
    override val entriesTaken: Int get() = 2

    companion object {
        const val TAG: Byte = 6
        fun fromDataInput(tag: Byte, input: DataInput): ConstantDoubleInfo {
            val highBytes = input.readInt()
            val lowBytes = input.readInt()
            return ConstantDoubleInfo(tag, highBytes, lowBytes)
        }
    }
}
