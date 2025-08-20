package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantMethodHandleInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val referenceKind: ReferenceKind,
    val referenceIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} ${referenceKind.line()} $referenceIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.METHOD_HANDLE

        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantMethodHandleInfo {
            val referenceKindUByte = input.readUnsignedByte().toUByte()
            val referenceKind = ReferenceKind.fromCode(referenceKindUByte)
            val referenceIndex = input.readUnsignedShort().toUShort()
            return ConstantMethodHandleInfo(tag, index, referenceKind, referenceIndex)
        }
    }
}