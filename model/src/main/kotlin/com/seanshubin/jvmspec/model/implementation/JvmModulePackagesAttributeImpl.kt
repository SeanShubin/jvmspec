package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeModulePackagesInfo
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmModulePackagesAttribute

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
