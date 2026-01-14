package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.specification.ConstantPoolTag
import java.io.DataInput

data class ConstantClassInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val nameIndex: UShort
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.CLASS
        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantClassInfo {
            val nameIndex = input.readUnsignedShort().toUShort()
            return ConstantClassInfo(tag, index, nameIndex)
        }
    }
}
