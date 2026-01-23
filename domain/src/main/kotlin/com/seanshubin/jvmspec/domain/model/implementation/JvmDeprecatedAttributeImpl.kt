package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeDeprecatedInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmDeprecatedAttribute

class JvmDeprecatedAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeDeprecatedInfo: AttributeDeprecatedInfo
) : JvmDeprecatedAttribute {
    override fun name(): String {
        return jvmClass.lookupUtf8(attributeDeprecatedInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeDeprecatedInfo.info
    }
}
