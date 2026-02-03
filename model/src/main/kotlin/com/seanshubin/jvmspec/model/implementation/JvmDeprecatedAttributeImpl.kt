package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeDeprecatedInfo
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmDeprecatedAttribute

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
