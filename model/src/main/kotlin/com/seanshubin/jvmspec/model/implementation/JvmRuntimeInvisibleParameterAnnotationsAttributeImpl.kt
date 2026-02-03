package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeRuntimeInvisibleParameterAnnotationsInfo
import com.seanshubin.jvmspec.classfile.structure.ParameterAnnotation
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmRuntimeInvisibleParameterAnnotationsAttribute

class JvmRuntimeInvisibleParameterAnnotationsAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeRuntimeInvisibleParameterAnnotationsInfo: AttributeRuntimeInvisibleParameterAnnotationsInfo
) : JvmRuntimeInvisibleParameterAnnotationsAttribute {
    override val numParameters: UByte = attributeRuntimeInvisibleParameterAnnotationsInfo.numParameters
    override val parameterAnnotations: List<ParameterAnnotation> =
        attributeRuntimeInvisibleParameterAnnotationsInfo.parameterAnnotations

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeRuntimeInvisibleParameterAnnotationsInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeRuntimeInvisibleParameterAnnotationsInfo.info
    }
}
