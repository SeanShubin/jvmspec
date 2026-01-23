package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeSourceDebugExtensionInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmSourceDebugExtensionAttribute

class JvmSourceDebugExtensionAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeSourceDebugExtensionInfo: AttributeSourceDebugExtensionInfo
) : JvmSourceDebugExtensionAttribute {
    override fun name(): String {
        return jvmClass.lookupUtf8(attributeSourceDebugExtensionInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeSourceDebugExtensionInfo.info
    }

    override fun debugExtension(): String {
        return String(attributeSourceDebugExtensionInfo.info.toByteArray(), Charsets.UTF_8)
    }
}
