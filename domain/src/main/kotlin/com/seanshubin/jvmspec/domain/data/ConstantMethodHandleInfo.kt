package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantMethodHandleInfo(
    override val tag: Byte,
    val referenceKind: Byte,
    val referenceIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 15

        fun fromDataInput(tag: Byte, input: DataInput): ConstantMethodHandleInfo {
            val referenceKind = input.readByte()
            val referenceIndex = input.readShort()
            return ConstantMethodHandleInfo(tag, referenceKind, referenceIndex)
        }
    }
}