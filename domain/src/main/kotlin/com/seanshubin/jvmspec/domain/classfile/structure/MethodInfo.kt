package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.specification.AccessFlag
import java.io.DataInput

data class MethodInfo(
    val accessFlags: Set<AccessFlag>,
    val nameIndex: UShort,
    val descriptorIndex: UShort,
    val attributesCount: UShort,
    val attributes: List<AttributeInfo>
) {
    companion object {
        fun fromDataInput(input: DataInput, constantPoolMap: Map<UShort, ConstantInfo>): MethodInfo {
            val accessFlagsMask = input.readUnsignedShort().toUShort()
            val accessFlags = AccessFlag.fromMask(accessFlagsMask)
            val nameIndex = input.readUnsignedShort().toUShort()
            val descriptorIndex = input.readUnsignedShort().toUShort()
            val attributesCount = input.readUnsignedShort().toUShort()
            val attributes =
                List(attributesCount.toInt()) { AttributeInfoFactory.fromDataInput(input, constantPoolMap) }
            return MethodInfo(accessFlags, nameIndex, descriptorIndex, attributesCount, attributes)
        }
    }
}
