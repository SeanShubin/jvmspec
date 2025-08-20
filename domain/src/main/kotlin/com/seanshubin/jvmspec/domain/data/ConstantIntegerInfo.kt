package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantIntegerInfo(
    override val tag: Byte,
    val bytes: Int
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 3

        fun fromDataInput(tag: Byte, input: DataInput): ConstantIntegerInfo {
            val bytes = input.readInt()
            return ConstantIntegerInfo(tag, bytes)
        }
    }
}