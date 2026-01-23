package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeAnnotationDefaultInfo
import com.seanshubin.jvmspec.domain.classfile.structure.ElementValue
import com.seanshubin.jvmspec.domain.model.api.JvmAnnotationDefaultAttribute
import com.seanshubin.jvmspec.domain.model.api.JvmClass

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
