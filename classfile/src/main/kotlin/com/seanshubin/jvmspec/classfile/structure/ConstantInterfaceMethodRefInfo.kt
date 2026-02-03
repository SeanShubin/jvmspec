package com.seanshubin.jvmspec.classfile.structure

import com.seanshubin.jvmspec.classfile.specification.ConstantPoolTag
import java.io.DataInput

data class ConstantInterfaceMethodRefInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    override val classIndex: UShort,
    override val nameAndTypeIndex: UShort
) : ConstantRefInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.INTERFACE_METHOD_REF

        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantInterfaceMethodRefInfo {
            val classIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantInterfaceMethodRefInfo(tag, index, classIndex, nameAndTypeIndex)
        }
    }
}