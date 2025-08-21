package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.indent
import java.io.DataInput

data class FieldInfo(
    val accessFlags: Set<AccessFlag>,
    val nameIndex: Short,
    val descriptorIndex: Short,
    val attributesCount: Short,
    val attributes: List<AttributeInfo>
) {
    fun lines(index: Int): List<String> {
        val header = listOf("Field[$index]")
        val content = listOf(
            "accessFlags=$accessFlags",
            "nameIndex=$nameIndex",
            "descriptorIndex=$descriptorIndex",
            "attributesCount=$attributesCount",
            *attributes.flatMapIndexed { index, attribute ->
                attribute.lines(index)
            }.map(indent).toTypedArray()
        ).map(indent)
        return header + content
    }
    companion object {
        fun fromDataInput(input: DataInput, constantPoolLookup: ConstantPoolLookup): FieldInfo {
            val accessFlagsMask = input.readUnsignedShort().toUShort()
            val accessFlags = AccessFlag.fromMask(accessFlagsMask)
            val nameIndex = input.readShort()
            val descriptorIndex = input.readShort()
            val attributesCount = input.readShort()
            val attributes =
                List(attributesCount.toInt()) { AttributeInfoFactory.fromDataInput(input, constantPoolLookup) }
            return FieldInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
        }
    }
}