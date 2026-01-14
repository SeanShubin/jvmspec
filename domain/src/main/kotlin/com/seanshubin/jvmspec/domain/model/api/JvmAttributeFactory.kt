package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeInfo

interface JvmAttributeFactory {
    fun createAttribute(jvmClass: JvmClass, attributeInfo: AttributeInfo): JvmAttribute
}
