package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag
import java.io.DataInput

data class ConstantMethodTypeInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val descriptorIndex: UShort
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.METHOD_TYPE

        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantMethodTypeInfo {
            val descriptorIndex = input.readUnsignedShort().toUShort()
            return ConstantMethodTypeInfo(tag, index, descriptorIndex)
        }
    }
}
