package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantMethodTypeInfo(
    override val tag: Byte,
    val descriptorIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 16

        fun fromDataInput(tag: Byte, input: DataInput): ConstantMethodTypeInfo {
            val descriptorIndex = input.readShort()
            return ConstantMethodTypeInfo(tag, descriptorIndex)
        }
    }
}
