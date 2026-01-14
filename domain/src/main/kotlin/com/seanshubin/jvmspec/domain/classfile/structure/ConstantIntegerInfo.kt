package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.specification.ConstantPoolTag
import java.io.DataInput

data class ConstantIntegerInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val intValue: Int
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.INTEGER

        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantIntegerInfo {
            val intValue = input.readInt()
            return ConstantIntegerInfo(tag, index, intValue)
        }
    }
}