package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeRuntimeVisibleParameterAnnotationsInfo
import com.seanshubin.jvmspec.domain.classfile.structure.ParameterAnnotation
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmRuntimeVisibleParameterAnnotationsAttribute

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
