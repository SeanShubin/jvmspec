package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.specification.ConstantPoolTag
import java.io.DataInput

data class ConstantMethodRefInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    override val classIndex: UShort,
    override val nameAndTypeIndex: UShort
) : ConstantRefInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.METHOD_REF

        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantMethodRefInfo {
            val classIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantMethodRefInfo(tag, index, classIndex, nameAndTypeIndex)
        }
    }
}