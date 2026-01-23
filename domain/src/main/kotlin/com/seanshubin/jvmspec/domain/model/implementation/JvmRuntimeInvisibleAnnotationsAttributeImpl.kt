package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.Annotation
import com.seanshubin.jvmspec.domain.classfile.structure.AttributeRuntimeInvisibleAnnotationsInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmRuntimeInvisibleAnnotationsAttribute

class JvmRuntimeInvisibleAnnotationsAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeRuntimeInvisibleAnnotationsInfo: AttributeRuntimeInvisibleAnnotationsInfo
) : JvmRuntimeInvisibleAnnotationsAttribute {
    override val numAnnotations: UShort = attributeRuntimeInvisibleAnnotationsInfo.numAnnotations
    override val annotations: List<Annotation> = attributeRuntimeInvisibleAnnotationsInfo.annotations

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeRuntimeInvisibleAnnotationsInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeRuntimeInvisibleAnnotationsInfo.info
    }
}
