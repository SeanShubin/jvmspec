package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeRuntimeInvisibleTypeAnnotationsInfo
import com.seanshubin.jvmspec.domain.classfile.structure.TypeAnnotation
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmRuntimeInvisibleTypeAnnotationsAttribute

class JvmRuntimeInvisibleTypeAnnotationsAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeRuntimeInvisibleTypeAnnotationsInfo: AttributeRuntimeInvisibleTypeAnnotationsInfo
) : JvmRuntimeInvisibleTypeAnnotationsAttribute {
    override val numAnnotations: UShort = attributeRuntimeInvisibleTypeAnnotationsInfo.numAnnotations
    override val annotations: List<TypeAnnotation> = attributeRuntimeInvisibleTypeAnnotationsInfo.annotations

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeRuntimeInvisibleTypeAnnotationsInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeRuntimeInvisibleTypeAnnotationsInfo.info
    }
}
