package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantModuleInfo(
    override val tag: ConstantPoolTag,
    val nameIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $nameIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.MODULE

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantModuleInfo {
            val nameIndex = input.readUnsignedShort().toUShort()
            return ConstantModuleInfo(tag, nameIndex)
        }
    }
}