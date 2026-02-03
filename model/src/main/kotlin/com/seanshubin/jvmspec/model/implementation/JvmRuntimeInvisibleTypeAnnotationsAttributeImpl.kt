package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeRuntimeInvisibleTypeAnnotationsInfo
import com.seanshubin.jvmspec.classfile.structure.TypeAnnotation
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmRuntimeInvisibleTypeAnnotationsAttribute

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
