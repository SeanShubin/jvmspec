package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantInterfaceMethodRefInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val classIndex: UShort,
    val nameAndTypeIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $classIndex $nameAndTypeIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.INTERFACE_METHOD_REF

        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantInterfaceMethodRefInfo {
            val classIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantInterfaceMethodRefInfo(tag, index, classIndex, nameAndTypeIndex)
        }
    }
}