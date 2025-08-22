package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.indent
import java.io.DataInput

data class MethodInfo(
    val accessFlags: UShort,
    val nameIndex: UShort,
    val descriptorIndex: UShort,
    val attributesCount: UShort,
    val attributes: List<AttributeInfo>
) {
    fun lines(index: Int, constantPoolLookup: ConstantPoolLookup): List<String> {
        val header = listOf("Method[$index]")
        val content = listOf(
            "accessFlags=$accessFlags",
            "name=${constantPoolLookup.utf8Line(nameIndex)}",
            "descriptor=${constantPoolLookup.utf8Line(descriptorIndex)}",
            "attributesCount=$attributesCount",
            *attributes.flatMapIndexed { index, attribute ->
                attribute.lines(index, constantPoolLookup)
            }.toTypedArray()
        ).map(indent)
        return header + content
    }

    companion object {
        fun fromDataInput(input: DataInput, constantPoolLookup: ConstantPoolLookup): MethodInfo {
            val accessFlags = input.readUnsignedShort().toUShort()
            val nameIndex = input.readUnsignedShort().toUShort()
            val descriptorIndex = input.readUnsignedShort().toUShort()
            val attributesCount = input.readUnsignedShort().toUShort()
            val attributes =
                List(attributesCount.toInt()) { AttributeInfoFactory.fromDataInput(input, constantPoolLookup) }
            return MethodInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
        }
    }
}
