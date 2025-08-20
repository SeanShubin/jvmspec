package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantIntegerInfo(
    override val tag: ConstantPoolTag,
    val intValue: Int
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $intValue"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.INTEGER

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantIntegerInfo {
            val intValue = input.readInt()
            return ConstantIntegerInfo(tag, intValue)
        }
    }
}