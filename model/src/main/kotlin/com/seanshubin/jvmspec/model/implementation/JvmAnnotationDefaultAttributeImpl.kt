package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AnnotationStructure.ElementValue
import com.seanshubin.jvmspec.classfile.structure.AttributeAnnotationDefaultInfo
import com.seanshubin.jvmspec.model.api.JvmAnnotationDefaultAttribute
import com.seanshubin.jvmspec.model.api.JvmClass

class JvmAnnotationDefaultAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeAnnotationDefaultInfo: AttributeAnnotationDefaultInfo
) : JvmAnnotationDefaultAttribute {
    override val defaultValue: ElementValue = attributeAnnotationDefaultInfo.defaultValue

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeAnnotationDefaultInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeAnnotationDefaultInfo.info
    }
}
