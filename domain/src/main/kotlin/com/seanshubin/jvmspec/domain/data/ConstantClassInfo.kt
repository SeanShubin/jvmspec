package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantClassInfo(
    override val tag: ConstantPoolTag,
    val nameIndex: Short
) : ConstantInfo {
    override fun line(): String {
        return "${tag.line()} $nameIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.CLASS
        fun fromDataInput(tag: ConstantPoolTag, input: DataInput): ConstantClassInfo {
            val nameIndex = input.readShort()
            return ConstantClassInfo(tag, nameIndex)
        }
    }
}
