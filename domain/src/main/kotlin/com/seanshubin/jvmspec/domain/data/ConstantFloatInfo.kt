package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantFloatInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val floatValue: Float
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $floatValue"
    }

    override fun annotatedLine(constantPoolLookup: ConstantPoolLookup): String = line()

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.FLOAT

        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantFloatInfo {
            val floatValue = input.readFloat()
            return ConstantFloatInfo(tag, index, floatValue)
        }
    }
}
