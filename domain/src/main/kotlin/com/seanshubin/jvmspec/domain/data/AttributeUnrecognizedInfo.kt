package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.indent
import com.seanshubin.jvmspec.domain.util.DataFormat.toHex

data class AttributeUnrecognizedInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>
) : AttributeInfo {
    override fun lines(index: Int, constantPoolLookup: ConstantPoolLookup): List<String> {
        val header = listOf("Attribute[$index]")
        val content = listOf(
            "attributeName=${constantPoolLookup.line(attributeIndex)}",
            "attributeLength=$attributeLength",
            "bytes=${info.toHex()}"
        ).map(indent)
        return header + content
    }

    companion object {
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolLookup: ConstantPoolLookup
        ): AttributeUnrecognizedInfo {
            return AttributeUnrecognizedInfo(
                attributeInfo.attributeIndex,
                attributeInfo.attributeLength,
                attributeInfo.info
            )
        }
    }
}