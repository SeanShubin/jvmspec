package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AnnotationStructure.Annotation
import com.seanshubin.jvmspec.classfile.structure.AttributeRuntimeVisibleAnnotationsInfo
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmRuntimeVisibleAnnotationsAttribute

class JvmRuntimeVisibleAnnotationsAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeRuntimeVisibleAnnotationsInfo: AttributeRuntimeVisibleAnnotationsInfo
) : JvmRuntimeVisibleAnnotationsAttribute {
    override val numAnnotations: UShort = attributeRuntimeVisibleAnnotationsInfo.numAnnotations
    override val annotations: List<Annotation> = attributeRuntimeVisibleAnnotationsInfo.annotations

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeRuntimeVisibleAnnotationsInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeRuntimeVisibleAnnotationsInfo.info
    }
}
