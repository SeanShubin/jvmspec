package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantNameAndTypeInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val nameIndex: UShort,
    val descriptorIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $nameIndex $descriptorIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.NAME_AND_TYPE

        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantNameAndTypeInfo {
            val nameIndex = input.readUnsignedShort().toUShort()
            val descriptorIndex = input.readUnsignedShort().toUShort()
            return ConstantNameAndTypeInfo(tag, index, nameIndex, descriptorIndex)
        }
    }
}
