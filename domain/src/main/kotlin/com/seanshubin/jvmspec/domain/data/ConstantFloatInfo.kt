package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantFloatInfo(
    override val tag: ConstantPoolTag,
    val bytes: Int
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $bytes"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.FLOAT

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantFloatInfo {
            val bytes = input.readInt()
            return ConstantFloatInfo(tag, bytes)
        }
    }
}
