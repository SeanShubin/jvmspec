package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantStringInfo(
    override val tag: ConstantPoolTag,
    val stringIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $stringIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.STRING

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantStringInfo {
            val stringIndex = input.readUnsignedShort().toUShort()
            return ConstantStringInfo(tag, stringIndex)
        }
    }
}