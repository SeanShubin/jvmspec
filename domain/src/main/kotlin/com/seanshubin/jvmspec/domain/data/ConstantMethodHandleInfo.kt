package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantMethodHandleInfo(
    override val tag: ConstantPoolTag,
    val referenceKind: Byte,
    val referenceIndex: Short
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $referenceKind $referenceIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.METHOD_HANDLE

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantMethodHandleInfo {
            val referenceKind = input.readByte()
            val referenceIndex = input.readShort()
            return ConstantMethodHandleInfo(tag, referenceKind, referenceIndex)
        }
    }
}