package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantMethodRefInfo(
    override val tag: Byte,
    val classIndex: Short,
    val nameAndTypeIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 10

        fun fromDataInput(tag: Byte, input: DataInput): ConstantMethodRefInfo {
            val classIndex = input.readShort()
            val nameAndTypeIndex = input.readShort()
            return ConstantMethodRefInfo(tag, classIndex, nameAndTypeIndex)
        }
    }
}