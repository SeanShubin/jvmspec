package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataInputExtensions.readByteList
import java.io.DataInput

data class AttributeInfo(
    val attributeNameIndex: Short,
    val attributeLength: Int,
    val info: List<Byte>
) {
    companion object {
        fun fromDataInput(input: DataInput): AttributeInfo {
            val attributeNameIndex = input.readShort()
            val attributeLength = input.readInt()
            val info = input.readByteList(attributeLength)
            return AttributeInfo(attributeNameIndex, attributeLength, info)
        }
    }
}