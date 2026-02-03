package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeRuntimeVisibleTypeAnnotationsInfo
import com.seanshubin.jvmspec.classfile.structure.TypeAnnotation
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmRuntimeVisibleTypeAnnotationsAttribute

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
