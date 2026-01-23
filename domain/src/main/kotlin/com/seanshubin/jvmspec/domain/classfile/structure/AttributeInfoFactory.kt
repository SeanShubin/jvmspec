package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.io.DataInputExtensions.readByteList
import java.io.DataInput

object AttributeInfoFactory {
    fun fromDataInput(input: DataInput, constantPoolMap: Map<UShort, ConstantInfo>): AttributeInfo {
        val attributeNameIndex = input.readUnsignedShort().toUShort()
        val attributeLength = input.readInt()
        val info = input.readByteList(attributeLength)
        val unrecognizedInfo = AttributeUnrecognizedInfo(
            attributeNameIndex,
            attributeLength,
            info
        )
        val attributeNameConstant = constantPoolMap.getValue(attributeNameIndex) as ConstantUtf8Info
        val factory = factoryMap[attributeNameConstant.utf8Value]
        return if (factory == null) unrecognizedInfo else factory(unrecognizedInfo, constantPoolMap, ::fromDataInput)
    }

    val factoryMap: Map<String, (AttributeInfo, Map<UShort, ConstantInfo>, (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo) -> AttributeInfo> =
        mapOf(
            AttributeCodeInfo.NAME to AttributeCodeInfo::fromAttributeInfo,
            AttributeSourceFileInfo.NAME to AttributeSourceFileInfo::fromAttributeInfo,
            AttributeConstantValueInfo.NAME to AttributeConstantValueInfo::fromAttributeInfo,
            AttributeDeprecatedInfo.NAME to AttributeDeprecatedInfo::fromAttributeInfo,
            AttributeSyntheticInfo.NAME to AttributeSyntheticInfo::fromAttributeInfo,
            AttributeSignatureInfo.NAME to AttributeSignatureInfo::fromAttributeInfo,
            AttributeExceptionsInfo.NAME to AttributeExceptionsInfo::fromAttributeInfo,
            AttributeLineNumberTableInfo.NAME to AttributeLineNumberTableInfo::fromAttributeInfo,
            AttributeLocalVariableTableInfo.NAME to AttributeLocalVariableTableInfo::fromAttributeInfo,
            AttributeLocalVariableTypeTableInfo.NAME to AttributeLocalVariableTypeTableInfo::fromAttributeInfo,
            AttributeSourceDebugExtensionInfo.NAME to AttributeSourceDebugExtensionInfo::fromAttributeInfo
        )
}