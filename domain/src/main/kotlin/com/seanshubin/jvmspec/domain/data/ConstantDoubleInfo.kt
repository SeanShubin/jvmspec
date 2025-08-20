package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantDoubleInfo(
    override val tag: ConstantPoolTag,
    val highBytes: Int,
    val lowBytes: Int
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $highBytes $lowBytes"
    }

    override val entriesTaken: Int get() = 2

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.DOUBLE
        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantDoubleInfo {
            val highBytes = input.readInt()
            val lowBytes = input.readInt()
            return ConstantDoubleInfo(tag, highBytes, lowBytes)
        }
    }
}
