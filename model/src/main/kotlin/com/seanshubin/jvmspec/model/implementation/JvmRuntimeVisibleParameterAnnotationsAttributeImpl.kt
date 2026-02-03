package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeRuntimeVisibleParameterAnnotationsInfo
import com.seanshubin.jvmspec.classfile.structure.ParameterAnnotation
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmRuntimeVisibleParameterAnnotationsAttribute

class JvmRuntimeVisibleParameterAnnotationsAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeRuntimeVisibleParameterAnnotationsInfo: AttributeRuntimeVisibleParameterAnnotationsInfo
) : JvmRuntimeVisibleParameterAnnotationsAttribute {
    override val numParameters: UByte = attributeRuntimeVisibleParameterAnnotationsInfo.numParameters
    override val parameterAnnotations: List<ParameterAnnotation> =
        attributeRuntimeVisibleParameterAnnotationsInfo.parameterAnnotations

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeRuntimeVisibleParameterAnnotationsInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeRuntimeVisibleParameterAnnotationsInfo.info
    }
}
