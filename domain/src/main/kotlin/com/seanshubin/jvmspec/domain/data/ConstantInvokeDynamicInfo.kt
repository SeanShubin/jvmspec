package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantInvokeDynamicInfo(
    override val tag: ConstantPoolTag,
    val bootstrapMethodAttrIndex: UShort,
    val nameAndTypeIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $bootstrapMethodAttrIndex $nameAndTypeIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.INVOKE_DYNAMIC
        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantInvokeDynamicInfo {
            val bootstrapMethodAttrIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantInvokeDynamicInfo(tag, bootstrapMethodAttrIndex, nameAndTypeIndex)
        }
    }
}
