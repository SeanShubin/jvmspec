package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.*
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmConstant
import com.seanshubin.jvmspec.domain.model.api.JvmModuleAttribute

class JvmModuleAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeModuleInfo: AttributeModuleInfo
) : JvmModuleAttribute {
    override val moduleNameIndex: UShort = attributeModuleInfo.moduleNameIndex
    override val moduleFlags: Int = attributeModuleInfo.moduleFlags
    override val moduleVersionIndex: UShort = attributeModuleInfo.moduleVersionIndex
    override val requiresCount: UShort = attributeModuleInfo.requiresCount
    override val requires: List<ModuleRequires> = attributeModuleInfo.requires
    override val exportsCount: UShort = attributeModuleInfo.exportsCount
    override val exports: List<ModuleExports> = attributeModuleInfo.exports
    override val opensCount: UShort = attributeModuleInfo.opensCount
    override val opens: List<ModuleOpens> = attributeModuleInfo.opens
    override val usesCount: UShort = attributeModuleInfo.usesCount
    override val usesIndex: List<UShort> = attributeModuleInfo.usesIndex
    override val providesCount: UShort = attributeModuleInfo.providesCount
    override val provides: List<ModuleProvides> = attributeModuleInfo.provides

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeModuleInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeModuleInfo.info
    }

    override fun moduleName(): String {
        val moduleConstant = jvmClass.constants.getValue(moduleNameIndex)
        return when (moduleConstant) {
            is JvmConstant.Module -> moduleConstant.moduleNameUtf8.value
            else -> "(invalid module constant)"
        }
    }

    override fun moduleVersion(): String {
        return if (moduleVersionIndex.toInt() == 0) {
            "(none)"
        } else {
            jvmClass.lookupUtf8(moduleVersionIndex)
        }
    }
}
