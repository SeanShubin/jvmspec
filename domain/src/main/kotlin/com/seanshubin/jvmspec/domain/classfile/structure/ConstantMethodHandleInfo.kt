package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.specification.ConstantPoolTag
import com.seanshubin.jvmspec.domain.classfile.specification.ReferenceKind
import java.io.DataInput

data class ConstantMethodHandleInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val referenceKind: ReferenceKind,
    val referenceIndex: UShort
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.METHOD_HANDLE

        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantMethodHandleInfo {
            val referenceKindUByte = input.readUnsignedByte().toUByte()
            val referenceKind = ReferenceKind.fromCode(referenceKindUByte)
            val referenceIndex = input.readUnsignedShort().toUShort()
            return ConstantMethodHandleInfo(tag, index, referenceKind, referenceIndex)
        }
    }
}