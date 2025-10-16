package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag
import java.io.DataInput

data class ConstantInvokeDynamicInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val bootstrapMethodAttrIndex: UShort,
    val nameAndTypeIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $bootstrapMethodAttrIndex $nameAndTypeIndex"
    }

    override fun annotatedLine(constantPoolLookup: ConstantPoolLookup): String {
        val nameAndTypeLine = constantPoolLookup.nameAndTypeLine(nameAndTypeIndex)
        return "[$index] ${tag.line()} $bootstrapMethodAttrIndex $nameAndTypeLine"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.INVOKE_DYNAMIC
        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantInvokeDynamicInfo {
            val bootstrapMethodAttrIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantInvokeDynamicInfo(tag, index, bootstrapMethodAttrIndex, nameAndTypeIndex)
        }
    }
}
