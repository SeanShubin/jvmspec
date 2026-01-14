package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.specification.ConstantPoolTag
import java.io.DataInput

data class ConstantDoubleInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val doubleValue: Double
) : ConstantInfo {
    override val entriesTaken: Int get() = 2

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.DOUBLE
        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantDoubleInfo {
            val value = input.readDouble()
            return ConstantDoubleInfo(tag, index, value)
        }
    }
}
