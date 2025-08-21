package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.indent
import com.seanshubin.jvmspec.domain.util.DataFormat.toHex

data class AttributeUnrecognizedInfo(
    override val attributeName: IndexName,
    override val attributeLength: Int,
    override val info: List<Byte>
) : AttributeInfo {
    override fun lines(index: Int): List<String> {
        val header = listOf("Attribute[$index]")
        val content = listOf(
            "attributeName=${attributeName.line()}",
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
                attributeInfo.attributeName,
                attributeInfo.attributeLength,
                attributeInfo.info
            )
        }
    }
}