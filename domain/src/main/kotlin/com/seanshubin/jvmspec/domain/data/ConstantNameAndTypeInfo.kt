package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantNameAndTypeInfo(
    override val tag: ConstantPoolTag,
    val nameIndex: Short,
    val descriptorIndex: Short
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $nameIndex $descriptorIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.NAME_AND_TYPE

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantNameAndTypeInfo {
            val nameIndex = input.readShort()
            val descriptorIndex = input.readShort()
            return ConstantNameAndTypeInfo(tag, nameIndex, descriptorIndex)
        }
    }
}
