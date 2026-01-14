package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.specification.ConstantPoolTag
import java.io.DataInput

data class ConstantStringInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val stringIndex: UShort
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.STRING

        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantStringInfo {
            val stringIndex = input.readUnsignedShort().toUShort()
            return ConstantStringInfo(tag, index, stringIndex)
        }
    }
}