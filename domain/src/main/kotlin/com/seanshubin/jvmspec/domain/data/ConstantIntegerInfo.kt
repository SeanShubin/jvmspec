package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantIntegerInfo(
    override val tag: ConstantPoolTag,
    val bytes: Int
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $bytes"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.INTEGER

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantIntegerInfo {
            val bytes = input.readInt()
            return ConstantIntegerInfo(tag, bytes)
        }
    }
}