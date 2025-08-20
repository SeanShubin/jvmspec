package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantInterfaceMethodRefInfo(
    override val tag: Byte,
    val classIndex: Short,
    val nameAndTypeIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 11

        fun fromDataInput(tag: Byte, input: DataInput): ConstantInterfaceMethodRefInfo {
            val classIndex = input.readShort()
            val nameAndTypeIndex = input.readShort()
            return ConstantInterfaceMethodRefInfo(tag, classIndex, nameAndTypeIndex)
        }
    }
}