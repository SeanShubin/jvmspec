package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class MethodInfo(
    val accessFlags: Short,
    val nameIndex: Short,
    val descriptorIndex: Short,
    val attributesCount: Short,
    val attributes: List<AttributeInfo>
) {
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
