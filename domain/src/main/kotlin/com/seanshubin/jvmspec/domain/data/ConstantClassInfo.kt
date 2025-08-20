package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantClassInfo(
    override val tag: Byte,
    val nameIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 7
        fun fromDataInput(tag: Byte, input: DataInput): ConstantClassInfo {
            val nameIndex = input.readShort()
            return ConstantClassInfo(tag, nameIndex)
        }
    }
}
