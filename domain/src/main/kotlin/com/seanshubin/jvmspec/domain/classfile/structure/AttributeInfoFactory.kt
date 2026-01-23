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
            AttributeStackMapTableInfo.NAME to AttributeStackMapTableInfo::fromAttributeInfo,
            AttributeSourceFileInfo.NAME to AttributeSourceFileInfo::fromAttributeInfo,
            AttributeConstantValueInfo.NAME to AttributeConstantValueInfo::fromAttributeInfo,
            AttributeDeprecatedInfo.NAME to AttributeDeprecatedInfo::fromAttributeInfo,
            AttributeSyntheticInfo.NAME to AttributeSyntheticInfo::fromAttributeInfo,
            AttributeSignatureInfo.NAME to AttributeSignatureInfo::fromAttributeInfo,
            AttributeExceptionsInfo.NAME to AttributeExceptionsInfo::fromAttributeInfo,
            AttributeLineNumberTableInfo.NAME to AttributeLineNumberTableInfo::fromAttributeInfo,
            AttributeLocalVariableTableInfo.NAME to AttributeLocalVariableTableInfo::fromAttributeInfo,
            AttributeLocalVariableTypeTableInfo.NAME to AttributeLocalVariableTypeTableInfo::fromAttributeInfo,
            AttributeSourceDebugExtensionInfo.NAME to AttributeSourceDebugExtensionInfo::fromAttributeInfo,
            AttributeInnerClassesInfo.NAME to AttributeInnerClassesInfo::fromAttributeInfo,
            AttributeEnclosingMethodInfo.NAME to AttributeEnclosingMethodInfo::fromAttributeInfo,
            AttributeNestHostInfo.NAME to AttributeNestHostInfo::fromAttributeInfo,
            AttributeNestMembersInfo.NAME to AttributeNestMembersInfo::fromAttributeInfo,
            AttributePermittedSubclassesInfo.NAME to AttributePermittedSubclassesInfo::fromAttributeInfo,
            AttributeBootstrapMethodsInfo.NAME to AttributeBootstrapMethodsInfo::fromAttributeInfo,
            AttributeMethodParametersInfo.NAME to AttributeMethodParametersInfo::fromAttributeInfo,
            AttributeModuleInfo.NAME to AttributeModuleInfo::fromAttributeInfo,
            AttributeModuleMainClassInfo.NAME to AttributeModuleMainClassInfo::fromAttributeInfo,
            AttributeModulePackagesInfo.NAME to AttributeModulePackagesInfo::fromAttributeInfo,
            AttributeModuleResolutionInfo.NAME to AttributeModuleResolutionInfo::fromAttributeInfo,
            AttributeModuleHashesInfo.NAME to AttributeModuleHashesInfo::fromAttributeInfo,
            AttributeRecordInfo.NAME to AttributeRecordInfo::fromAttributeInfo,
            AttributeRuntimeVisibleAnnotationsInfo.NAME to AttributeRuntimeVisibleAnnotationsInfo::fromAttributeInfo,
            AttributeRuntimeInvisibleAnnotationsInfo.NAME to AttributeRuntimeInvisibleAnnotationsInfo::fromAttributeInfo,
            AttributeRuntimeVisibleParameterAnnotationsInfo.NAME to AttributeRuntimeVisibleParameterAnnotationsInfo::fromAttributeInfo,
            AttributeRuntimeInvisibleParameterAnnotationsInfo.NAME to AttributeRuntimeInvisibleParameterAnnotationsInfo::fromAttributeInfo,
            AttributeRuntimeVisibleTypeAnnotationsInfo.NAME to AttributeRuntimeVisibleTypeAnnotationsInfo::fromAttributeInfo,
            AttributeRuntimeInvisibleTypeAnnotationsInfo.NAME to AttributeRuntimeInvisibleTypeAnnotationsInfo::fromAttributeInfo,
            AttributeAnnotationDefaultInfo.NAME to AttributeAnnotationDefaultInfo::fromAttributeInfo
        )
}