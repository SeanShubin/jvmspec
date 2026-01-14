package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeCodeInfo
import com.seanshubin.jvmspec.domain.classfile.structure.AttributeInfo
import com.seanshubin.jvmspec.domain.model.api.JvmAttribute
import com.seanshubin.jvmspec.domain.model.api.JvmAttributeFactory
import com.seanshubin.jvmspec.domain.model.api.JvmClass

class JvmAttributeFactoryImpl : JvmAttributeFactory {
    override fun createAttribute(jvmClass: JvmClass, attributeInfo: AttributeInfo): JvmAttribute =
        when (attributeInfo) {
            is AttributeCodeInfo -> JvmCodeAttributeImpl(jvmClass, attributeInfo)
            else -> JvmAttributeImpl(jvmClass, attributeInfo)
        }
}
