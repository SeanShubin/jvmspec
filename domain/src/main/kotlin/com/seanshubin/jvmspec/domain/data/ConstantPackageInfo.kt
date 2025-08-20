package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantPackageInfo(
    override val tag: ConstantPoolTag,
    val nameIndex: Short
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $nameIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.PACKAGE

        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantPackageInfo {
            val nameIndex = input.readShort()
            return ConstantPackageInfo(tag, nameIndex)
        }
    }
}