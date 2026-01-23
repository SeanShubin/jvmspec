package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeBootstrapMethodsInfo
import com.seanshubin.jvmspec.domain.classfile.structure.BootstrapMethod
import com.seanshubin.jvmspec.domain.model.api.JvmBootstrapMethodsAttribute
import com.seanshubin.jvmspec.domain.model.api.JvmClass

class JvmBootstrapMethodsAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeBootstrapMethodsInfo: AttributeBootstrapMethodsInfo
) : JvmBootstrapMethodsAttribute {
    override val numBootstrapMethods: UShort = attributeBootstrapMethodsInfo.numBootstrapMethods
    override val bootstrapMethods: List<BootstrapMethod> = attributeBootstrapMethodsInfo.bootstrapMethods

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeBootstrapMethodsInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeBootstrapMethodsInfo.info
    }
}
