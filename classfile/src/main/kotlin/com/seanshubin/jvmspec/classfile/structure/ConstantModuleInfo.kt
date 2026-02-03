package com.seanshubin.jvmspec.classfile.structure

import com.seanshubin.jvmspec.classfile.specification.ConstantPoolTag
import java.io.DataInput

data class ConstantModuleInfo(
    override val tag: ConstantPoolTag,
    override val index: UShort,
    val nameIndex: UShort
) : ConstantInfo {
    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.MODULE

        fun fromDataInput(tag: ConstantPoolTag, index: UShort, input: DataInput): ConstantModuleInfo {
            val nameIndex = input.readUnsignedShort().toUShort()
            return ConstantModuleInfo(tag, index, nameIndex)
        }
    }
}