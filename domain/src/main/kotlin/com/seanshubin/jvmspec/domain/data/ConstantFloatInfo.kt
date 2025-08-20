package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantFloatInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val bytes: Int
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $bytes"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.FLOAT

        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantFloatInfo {
            val bytes = input.readInt()
            return ConstantFloatInfo(tag, index, bytes)
        }
    }
}
