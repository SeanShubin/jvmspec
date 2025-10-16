package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag
import java.io.DataInput

data class ConstantModuleInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val nameIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $nameIndex"
    }

    override fun annotatedLine(constantPoolLookup: ConstantPoolLookup): String {
        val nameLine = constantPoolLookup.utf8Line(nameIndex)
        return "[$index] ${tag.line()} $nameLine"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.MODULE

        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantModuleInfo {
            val nameIndex = input.readUnsignedShort().toUShort()
            return ConstantModuleInfo(tag, index, nameIndex)
        }
    }
}