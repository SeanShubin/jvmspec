package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeNestHostInfo
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmNestHostAttribute

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
