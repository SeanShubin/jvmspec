package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataInputExtensions.readByteList
import java.io.DataInput

object AttributeInfoFactory {
    fun fromDataInput(input: DataInput, constantPoolLookup: ConstantPoolLookup): AttributeInfo {
        val attributeNameIndex = input.readUnsignedShort().toUShort()
        val attributeName = IndexName.fromIndex(attributeNameIndex, constantPoolLookup)
        val attributeLength = input.readInt()
        val info = input.readByteList(attributeLength)
        val unrecognizedInfo = AttributeUnrecognizedInfo(
            attributeName,
            attributeLength,
            info
        )
        val factory = factoryMap[attributeName.name]
        return if (factory == null) unrecognizedInfo else factory(unrecognizedInfo, constantPoolLookup)
    }

    val factoryMap: Map<String, (AttributeInfo, ConstantPoolLookup) -> AttributeInfo> = mapOf(
        AttributeCodeInfo.NAME to AttributeCodeInfo::fromAttributeInfo
    )
}