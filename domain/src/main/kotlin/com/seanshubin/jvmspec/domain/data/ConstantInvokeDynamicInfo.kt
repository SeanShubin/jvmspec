package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantInvokeDynamicInfo(
    override val tag: ConstantPoolTag,
    val bootstrapMethodAttrIndex: Short,
    val nameAndTypeIndex: Short
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $bootstrapMethodAttrIndex $nameAndTypeIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.INVOKE_DYNAMIC
        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantInvokeDynamicInfo {
            val bootstrapMethodAttrIndex = input.readShort()
            val nameAndTypeIndex = input.readShort()
            return ConstantInvokeDynamicInfo(tag, bootstrapMethodAttrIndex, nameAndTypeIndex)
        }
    }
}
