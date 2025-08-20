package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantMethodTypeInfo(
    override val tag: ConstantPoolTag,
    val descriptorIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $descriptorIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.METHOD_TYPE

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantMethodTypeInfo {
            val descriptorIndex = input.readUnsignedShort().toUShort()
            return ConstantMethodTypeInfo(tag, descriptorIndex)
        }
    }
}
