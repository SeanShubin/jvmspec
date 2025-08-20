package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantClassInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val nameIndex: Short
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $nameIndex"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.CLASS
        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantClassInfo {
            val nameIndex = input.readShort()
            return ConstantClassInfo(tag, index, nameIndex)
        }
    }
}
