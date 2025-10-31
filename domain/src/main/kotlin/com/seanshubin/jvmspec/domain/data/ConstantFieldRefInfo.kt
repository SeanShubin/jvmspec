package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag
import java.io.DataInput

data class ConstantFieldRefInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    override val classIndex: UShort,
    override val nameAndTypeIndex: UShort
) : ConstantRefInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.FIELD_REF

        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantFieldRefInfo {
            val classIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantFieldRefInfo(tag, index, classIndex, nameAndTypeIndex)
        }
    }
}