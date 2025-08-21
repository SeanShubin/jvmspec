package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.indent
import com.seanshubin.jvmspec.domain.util.DataFormat.toHex

data class AttributeUnrecognizedInfo(
    override val attributeNameIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>
) : AttributeInfo {
    override fun lines(index: Int): List<String> {
        val header = listOf("Attribute.Unrecognized[$index]")
        val content = listOf(
            "attributeNameIndex=$attributeNameIndex",
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
                attributeInfo.attributeNameIndex,
                attributeInfo.attributeLength,
                attributeInfo.info
            )
        }
    }
}