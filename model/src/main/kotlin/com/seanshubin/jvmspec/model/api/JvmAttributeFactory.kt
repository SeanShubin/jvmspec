package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.AttributeInfo

interface JvmAttributeFactory {
    fun createAttribute(jvmClass: JvmClass, attributeInfo: AttributeInfo): JvmAttribute
}
