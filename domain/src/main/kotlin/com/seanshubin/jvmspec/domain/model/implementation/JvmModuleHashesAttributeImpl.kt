package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeModuleHashesInfo
import com.seanshubin.jvmspec.domain.classfile.structure.ModuleHash
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmModuleHashesAttribute

class JvmModuleHashesAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeModuleHashesInfo: AttributeModuleHashesInfo
) : JvmModuleHashesAttribute {
    override val algorithmIndex: UShort = attributeModuleHashesInfo.algorithmIndex
    override val modulesCount: UShort = attributeModuleHashesInfo.modulesCount
    override val modules: List<ModuleHash> = attributeModuleHashesInfo.modules

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeModuleHashesInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeModuleHashesInfo.info
    }

    override fun algorithm(): String {
        return jvmClass.lookupUtf8(algorithmIndex)
    }
}
