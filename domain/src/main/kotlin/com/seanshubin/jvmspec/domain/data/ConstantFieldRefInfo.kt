package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantFieldRefInfo(
    override val tag: Byte,
    val classIndex: Short,
    val nameAndTypeIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 9

        fun fromDataInput(tag: Byte, input: DataInput): ConstantFieldRefInfo {
            val classIndex = input.readShort()
            val nameAndTypeIndex = input.readShort()
            return ConstantFieldRefInfo(tag, classIndex, nameAndTypeIndex)
        }
    }
}