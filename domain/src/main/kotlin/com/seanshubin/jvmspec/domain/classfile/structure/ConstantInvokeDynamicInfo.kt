package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.specification.ConstantPoolTag
import java.io.DataInput

data class ConstantInvokeDynamicInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val bootstrapMethodAttrIndex: UShort,
    val nameAndTypeIndex: UShort
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.INVOKE_DYNAMIC
        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantInvokeDynamicInfo {
            val bootstrapMethodAttrIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantInvokeDynamicInfo(tag, index, bootstrapMethodAttrIndex, nameAndTypeIndex)
        }
    }
}
