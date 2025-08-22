package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.indent
import java.io.DataInput

data class FieldInfo(
    val accessFlags: Set<AccessFlag>,
    val nameIndex: UShort,
    val descriptorIndex: UShort,
    val attributesCount: UShort,
    val attributes: List<AttributeInfo>
) {
    fun lines(index: Int, constantPoolLookup: ConstantPoolLookup): List<String> {
        val header = listOf("Field[$index]")
        val content = listOf(
            "accessFlags=$accessFlags",
            "name=${constantPoolLookup.line(nameIndex)}",
            "descriptor=${constantPoolLookup.line(descriptorIndex)}",
            "attributesCount=$attributesCount",
            *attributes.flatMapIndexed { index, attribute ->
                attribute.lines(index, constantPoolLookup)
            }.map(indent).toTypedArray()
        ).map(indent)
        return header + content
    }

    companion object {
        fun fromDataInput(input: DataInput, constantPoolLookup: ConstantPoolLookup): FieldInfo {
            val accessFlagsMask = input.readUnsignedShort().toUShort()
            val accessFlags = AccessFlag.fromMask(accessFlagsMask)
            val nameIndex = input.readUnsignedShort().toUShort()
            val descriptorIndex = input.readUnsignedShort().toUShort()
            val attributesCount = input.readUnsignedShort().toUShort()
            val attributes =
                List(attributesCount.toInt()) { AttributeInfoFactory.fromDataInput(input, constantPoolLookup) }
            return FieldInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
        }
    }
}