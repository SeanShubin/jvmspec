package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantDoubleInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val doubleValue: Double
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $doubleValue"
    }

    override val entriesTaken: Int get() = 2

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.DOUBLE
        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantDoubleInfo {
            val value = input.readDouble()
            return ConstantDoubleInfo(tag, index, value)
        }
    }
}
