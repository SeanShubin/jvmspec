package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantFieldRefInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val classIndex: Short,
    val nameAndTypeIndex: Short
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $classIndex $nameAndTypeIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.FIELD_REF

        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantFieldRefInfo {
            val classIndex = input.readShort()
            val nameAndTypeIndex = input.readShort()
            return ConstantFieldRefInfo(tag, index, classIndex, nameAndTypeIndex)
        }
    }
}