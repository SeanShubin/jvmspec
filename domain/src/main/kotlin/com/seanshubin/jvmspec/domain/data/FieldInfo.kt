package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.indent
import java.io.DataInput

data class FieldInfo(
    val accessFlags: Set<AccessFlag>,
    val name: IndexName,
    val descriptor: IndexName,
    val attributesCount: UShort,
    val attributes: List<AttributeInfo>
) {
    fun lines(index: Int): List<String> {
        val header = listOf("Field[$index]")
        val content = listOf(
            "accessFlags=$accessFlags",
            "name=${name.line()}",
            "descriptor=${descriptor.line()}",
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
            val nameIndex = input.readUnsignedShort().toUShort()
            val name = IndexName.fromIndex(nameIndex, constantPoolLookup)
            val descriptorIndex = input.readUnsignedShort().toUShort()
            val descriptor = IndexName.fromIndex(descriptorIndex, constantPoolLookup)
            val attributesCount = input.readUnsignedShort().toUShort()
            val attributes =
                List(attributesCount.toInt()) { AttributeInfoFactory.fromDataInput(input, constantPoolLookup) }
            return FieldInfo(accessFlags, name, descriptor, attributesCount, attributes)
        }
    }
}