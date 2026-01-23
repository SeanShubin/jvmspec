package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeModuleMainClassInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmModuleMainClassAttribute

class JvmModuleMainClassAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeModuleMainClassInfo: AttributeModuleMainClassInfo
) : JvmModuleMainClassAttribute {
    override val mainClassIndex: UShort = attributeModuleMainClassInfo.mainClassIndex

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeModuleMainClassInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeModuleMainClassInfo.info
    }

    override fun mainClassName(): String {
        return jvmClass.lookupClassName(mainClassIndex)
    }
}
