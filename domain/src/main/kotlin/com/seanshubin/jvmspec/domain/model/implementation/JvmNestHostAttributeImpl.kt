package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeNestHostInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmNestHostAttribute

class JvmNestHostAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeNestHostInfo: AttributeNestHostInfo
) : JvmNestHostAttribute {
    override val hostClassIndex: UShort = attributeNestHostInfo.hostClassIndex

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeNestHostInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeNestHostInfo.info
    }

    override fun hostClassName(): String {
        return jvmClass.lookupClassName(hostClassIndex)
    }
}
