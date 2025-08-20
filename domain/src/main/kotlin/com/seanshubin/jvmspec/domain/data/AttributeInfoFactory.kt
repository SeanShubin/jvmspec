package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataInputExtensions.readByteList
import java.io.DataInput

object AttributeInfoFactory {
    fun fromDataInput(input: DataInput, constantPoolLookup: ConstantPoolLookup): AttributeInfo {
        val attributeNameIndex = input.readShort()
        val attributeLength = input.readInt()
        val info = input.readByteList(attributeLength)
        val attributeName = constantPoolLookup.getUtf8(attributeNameIndex)
        val unrecognizedInfo = AttributeUnrecognizedInfo(
            attributeNameIndex,
            attributeLength,
            info
        )
        val factory = factoryMap[attributeName]
        return if (factory == null) unrecognizedInfo else factory(unrecognizedInfo, constantPoolLookup)
    }

    val factoryMap: Map<String, (AttributeInfo, ConstantPoolLookup) -> AttributeInfo> = mapOf(
        AttributeCodeInfo.NAME to AttributeCodeInfo::fromAttributeInfo
    )
}