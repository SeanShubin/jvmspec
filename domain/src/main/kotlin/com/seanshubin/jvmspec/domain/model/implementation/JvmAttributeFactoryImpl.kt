package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.*
import com.seanshubin.jvmspec.domain.model.api.JvmAttribute
import com.seanshubin.jvmspec.domain.model.api.JvmAttributeFactory
import com.seanshubin.jvmspec.domain.model.api.JvmClass

class JvmAttributeFactoryImpl : JvmAttributeFactory {
    override fun createAttribute(jvmClass: JvmClass, attributeInfo: AttributeInfo): JvmAttribute =
        when (attributeInfo) {
            is AttributeCodeInfo -> JvmCodeAttributeImpl(jvmClass, attributeInfo)
            is AttributeSourceFileInfo -> JvmSourceFileAttributeImpl(jvmClass, attributeInfo)
            is AttributeConstantValueInfo -> JvmConstantValueAttributeImpl(jvmClass, attributeInfo)
            is AttributeDeprecatedInfo -> JvmDeprecatedAttributeImpl(jvmClass, attributeInfo)
            is AttributeSyntheticInfo -> JvmSyntheticAttributeImpl(jvmClass, attributeInfo)
            is AttributeSignatureInfo -> JvmSignatureAttributeImpl(jvmClass, attributeInfo)
            is AttributeExceptionsInfo -> JvmExceptionsAttributeImpl(jvmClass, attributeInfo)
            is AttributeLineNumberTableInfo -> JvmLineNumberTableAttributeImpl(jvmClass, attributeInfo)
            is AttributeLocalVariableTableInfo -> JvmLocalVariableTableAttributeImpl(jvmClass, attributeInfo)
            is AttributeLocalVariableTypeTableInfo -> JvmLocalVariableTypeTableAttributeImpl(jvmClass, attributeInfo)
            is AttributeSourceDebugExtensionInfo -> JvmSourceDebugExtensionAttributeImpl(jvmClass, attributeInfo)
            is AttributeInnerClassesInfo -> JvmInnerClassesAttributeImpl(jvmClass, attributeInfo)
            is AttributeEnclosingMethodInfo -> JvmEnclosingMethodAttributeImpl(jvmClass, attributeInfo)
            is AttributeNestHostInfo -> JvmNestHostAttributeImpl(jvmClass, attributeInfo)
            is AttributeNestMembersInfo -> JvmNestMembersAttributeImpl(jvmClass, attributeInfo)
            is AttributePermittedSubclassesInfo -> JvmPermittedSubclassesAttributeImpl(jvmClass, attributeInfo)
            is AttributeBootstrapMethodsInfo -> JvmBootstrapMethodsAttributeImpl(jvmClass, attributeInfo)
            is AttributeMethodParametersInfo -> JvmMethodParametersAttributeImpl(jvmClass, attributeInfo)
            is AttributeModuleMainClassInfo -> JvmModuleMainClassAttributeImpl(jvmClass, attributeInfo)
            is AttributeModulePackagesInfo -> JvmModulePackagesAttributeImpl(jvmClass, attributeInfo)
            is AttributeRecordInfo -> JvmRecordAttributeImpl(jvmClass, attributeInfo)
            is AttributeRuntimeVisibleAnnotationsInfo -> JvmRuntimeVisibleAnnotationsAttributeImpl(
                jvmClass,
                attributeInfo
            )

            is AttributeRuntimeInvisibleAnnotationsInfo -> JvmRuntimeInvisibleAnnotationsAttributeImpl(
                jvmClass,
                attributeInfo
            )

            is AttributeRuntimeVisibleParameterAnnotationsInfo -> JvmRuntimeVisibleParameterAnnotationsAttributeImpl(
                jvmClass,
                attributeInfo
            )

            is AttributeRuntimeInvisibleParameterAnnotationsInfo -> JvmRuntimeInvisibleParameterAnnotationsAttributeImpl(
                jvmClass,
                attributeInfo
            )

            is AttributeRuntimeVisibleTypeAnnotationsInfo -> JvmRuntimeVisibleTypeAnnotationsAttributeImpl(
                jvmClass,
                attributeInfo
            )

            is AttributeRuntimeInvisibleTypeAnnotationsInfo -> JvmRuntimeInvisibleTypeAnnotationsAttributeImpl(
                jvmClass,
                attributeInfo
            )

            is AttributeAnnotationDefaultInfo -> JvmAnnotationDefaultAttributeImpl(jvmClass, attributeInfo)
            else -> JvmAttributeImpl(jvmClass, attributeInfo)
        }
}
