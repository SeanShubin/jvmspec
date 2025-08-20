package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantDynamicInfo(
    override val tag: Byte,
    val bootstrapMethodAttrIndex: Short,
    val nameAndTypeIndex: Short
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        const val TAG: Byte = 17
        fun fromDataInput(tag: Byte, input: DataInput): ConstantDynamicInfo {
            val bootstrapMethodAttrIndex = input.readShort()
            val nameAndTypeIndex = input.readShort()
            return ConstantDynamicInfo(tag, bootstrapMethodAttrIndex, nameAndTypeIndex)
        }
    }
}
