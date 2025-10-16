package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.primitive.ConstantPoolTag
import java.io.DataInput

data class ConstantMethodTypeInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    val descriptorIndex: UShort
) : ConstantInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $descriptorIndex"
    }

    override fun annotatedLine(constantPoolLookup: ConstantPoolLookup): String {
        val descriptorLine = constantPoolLookup.utf8Line(descriptorIndex)
        return "[$index] ${tag.line()} $descriptorLine"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.METHOD_TYPE

        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantMethodTypeInfo {
            val descriptorIndex = input.readUnsignedShort().toUShort()
            return ConstantMethodTypeInfo(tag, index, descriptorIndex)
        }
    }
}
