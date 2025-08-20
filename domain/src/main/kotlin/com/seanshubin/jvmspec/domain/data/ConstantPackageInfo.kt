package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantPackageInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val nameIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $nameIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.PACKAGE

        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantPackageInfo {
            val nameIndex = input.readUnsignedShort().toUShort()
            return ConstantPackageInfo(tag, index, nameIndex)
        }
    }
}