package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeModulePackagesInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmModulePackagesAttribute

class JvmModulePackagesAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeModulePackagesInfo: AttributeModulePackagesInfo
) : JvmModulePackagesAttribute {
    override val packageCount: UShort = attributeModulePackagesInfo.packageCount
    override val packageIndex: List<UShort> = attributeModulePackagesInfo.packageIndex

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeModulePackagesInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeModulePackagesInfo.info
    }
}
