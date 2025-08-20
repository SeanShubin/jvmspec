package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantNameAndTypeInfo(
    override val tag: Byte,
    val nameIndex: Short,
    val descriptorIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 12

        fun fromDataInput(tag: Byte, input: DataInput): ConstantNameAndTypeInfo {
            val nameIndex = input.readShort()
            val descriptorIndex = input.readShort()
            return ConstantNameAndTypeInfo(tag, nameIndex, descriptorIndex)
        }
    }
}
