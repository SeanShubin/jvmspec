package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantLongInfo(
    override val tag: ConstantPoolTag,
    val longValue: Long
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $longValue"
    }

    override val entriesTaken: Int get() = 2

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.LONG

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantLongInfo {
            val longValue = input.readLong()
            return ConstantLongInfo(tag, longValue)
        }
    }
}
