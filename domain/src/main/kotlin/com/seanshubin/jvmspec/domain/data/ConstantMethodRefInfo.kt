package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ConstantMethodRefInfo(
    override val tag: ConstantPoolTag,
    override val index: Int,
    override val classIndex: UShort,
    override val nameAndTypeIndex: UShort
) : ConstantRefInfo {
    override fun line(): String {
        return "[$index] ${tag.line()} $classIndex $nameAndTypeIndex"
    }

    override fun annotatedLine(constantPoolLookup: ConstantPoolLookup): String {
        val classLine = constantPoolLookup.classLine(classIndex)
        val nameAndTypeLine = constantPoolLookup.nameAndTypeLine(nameAndTypeIndex)
        return "[$index] ${tag.line()} $classLine $nameAndTypeLine"
    }

    override val entriesTaken: Int get() = 1

    companion object {
        val TAG: ConstantPoolTag = ConstantPoolTag.METHOD_REF

        fun fromDataInput(tag: ConstantPoolTag, index: Int, input: DataInput): ConstantMethodRefInfo {
            val classIndex = input.readUnsignedShort().toUShort()
            val nameAndTypeIndex = input.readUnsignedShort().toUShort()
            return ConstantMethodRefInfo(tag, index, classIndex, nameAndTypeIndex)
        }
    }
}