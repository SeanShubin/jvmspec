package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class FieldInfo(
    val accessFlags: Short,
    val nameIndex: Short,
    val descriptorIndex: Short,
    val attributesCount: Short,
    val attributes: List<AttributeInfo>
) {
    companion object {
        fun fromDataInput(input: DataInput): FieldInfo {
            val accessFlags = input.readShort()
            val nameIndex = input.readShort()
            val descriptorIndex = input.readShort()
            val attributesCount = input.readShort()
            val attributes = List(attributesCount.toInt()) { AttributeInfo.fromDataInput(input) }
            return FieldInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
        }
    }
}