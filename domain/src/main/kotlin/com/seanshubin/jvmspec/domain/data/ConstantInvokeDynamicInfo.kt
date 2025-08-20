package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantInvokeDynamicInfo(
    override val tag: Byte,
    val bootstrapMethodAttrIndex: Short,
    val nameAndTypeIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 18
        fun fromDataInput(tag: Byte, input: DataInput): ConstantInvokeDynamicInfo {
            val bootstrapMethodAttrIndex = input.readShort()
            val nameAndTypeIndex = input.readShort()
            return ConstantInvokeDynamicInfo(tag, bootstrapMethodAttrIndex, nameAndTypeIndex)
        }
    }
}
