package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantMethodRefInfo(
    override val tag: ConstantPoolTag,
    val classIndex: UShort,
    val nameAndTypeIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $classIndex $nameAndTypeIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.METHOD_REF

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantMethodRefInfo {
            val classIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantMethodRefInfo(tag, classIndex, nameAndTypeIndex)
        }
    }
}