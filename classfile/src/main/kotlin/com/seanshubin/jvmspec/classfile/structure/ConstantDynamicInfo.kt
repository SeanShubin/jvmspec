package com.seanshubin.jvmspec.classfile.structure

import com.seanshubin.jvmspec.classfile.specification.ConstantPoolTag
import java.io.DataInput

data class ConstantDynamicInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val bootstrapMethodAttrIndex: UShort,
    val nameAndTypeIndex: UShort
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.DYNAMIC
        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantDynamicInfo {
            val bootstrapMethodAttrIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantDynamicInfo(tag, index, bootstrapMethodAttrIndex, nameAndTypeIndex)
        }
    }
}
