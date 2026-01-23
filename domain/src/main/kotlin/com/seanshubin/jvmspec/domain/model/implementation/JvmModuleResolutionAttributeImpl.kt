package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeModuleResolutionInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmModuleResolutionAttribute

class JvmModuleResolutionAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeModuleResolutionInfo: AttributeModuleResolutionInfo
) : JvmModuleResolutionAttribute {
    override val resolutionFlags: Int = attributeModuleResolutionInfo.resolutionFlags

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeModuleResolutionInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeModuleResolutionInfo.info
    }
}
