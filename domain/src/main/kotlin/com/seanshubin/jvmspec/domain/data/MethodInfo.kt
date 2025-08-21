package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.indent
import java.io.DataInput

data class MethodInfo(
    val accessFlags: Short,
    val nameIndex: Short,
    val descriptorIndex: Short,
    val attributesCount: Short,
    val attributes: List<AttributeInfo>
) {
    fun lines(index: Int): List<String> {
        val header = listOf("Method[$index]")
        val content = listOf(
            "accessFlags=$accessFlags",
            "nameIndex=$nameIndex",
            "descriptorIndex=$descriptorIndex",
            "attributesCount=$attributesCount",
            *attributes.flatMapIndexed { index, attribute ->
                attribute.lines(index)
            }.toTypedArray()
        ).map(indent)
        return header + content
    }

    companion object {
        fun fromDataInput(input: DataInput, constantPoolLookup: ConstantPoolLookup): MethodInfo {
            val accessFlags = input.readShort()
            val nameIndex = input.readShort()
            val descriptorIndex = input.readShort()
            val attributesCount = input.readShort()
            val attributes =
                List(attributesCount.toInt()) { AttributeInfoFactory.fromDataInput(input, constantPoolLookup) }
            return MethodInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
        }
    }
}
