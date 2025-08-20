package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantDynamicInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val bootstrapMethodAttrIndex: Short,
    val nameAndTypeIndex: Short
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $bootstrapMethodAttrIndex $nameAndTypeIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.DYNAMIC
        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantDynamicInfo {
            val bootstrapMethodAttrIndex = input.readShort()
            val nameAndTypeIndex = input.readShort()
            return ConstantDynamicInfo(tag, index, bootstrapMethodAttrIndex, nameAndTypeIndex)
        }
    }
}
