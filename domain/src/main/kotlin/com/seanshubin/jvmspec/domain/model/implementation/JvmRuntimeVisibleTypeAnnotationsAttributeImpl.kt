package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeRuntimeVisibleTypeAnnotationsInfo
import com.seanshubin.jvmspec.domain.classfile.structure.TypeAnnotation
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmRuntimeVisibleTypeAnnotationsAttribute

class JvmRuntimeVisibleTypeAnnotationsAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeRuntimeVisibleTypeAnnotationsInfo: AttributeRuntimeVisibleTypeAnnotationsInfo
) : JvmRuntimeVisibleTypeAnnotationsAttribute {
    override val numAnnotations: UShort = attributeRuntimeVisibleTypeAnnotationsInfo.numAnnotations
    override val annotations: List<TypeAnnotation> = attributeRuntimeVisibleTypeAnnotationsInfo.annotations

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeRuntimeVisibleTypeAnnotationsInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeRuntimeVisibleTypeAnnotationsInfo.info
    }
}
