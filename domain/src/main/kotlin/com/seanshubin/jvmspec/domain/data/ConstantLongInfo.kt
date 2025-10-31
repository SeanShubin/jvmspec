package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag
import java.io.DataInput

data class ConstantLongInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val longValue: Long
) : ConstantInfo {
    override val entriesTaken: Int get() = 2

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.LONG

        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantLongInfo {
            val longValue = input.readLong()
            return ConstantLongInfo(tag, index, longValue)
        }
    }
}
