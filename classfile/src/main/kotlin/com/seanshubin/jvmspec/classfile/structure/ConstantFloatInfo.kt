package com.seanshubin.jvmspec.classfile.structure

import com.seanshubin.jvmspec.classfile.specification.ConstantPoolTag
import java.io.DataInput

data class ConstantFloatInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val floatValue: Float
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.FLOAT

        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantFloatInfo {
            val floatValue = input.readFloat()
            return ConstantFloatInfo(tag, index, floatValue)
        }
    }
}
