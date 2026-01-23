package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeEnclosingMethodInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmEnclosingMethodAttribute

class JvmEnclosingMethodAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeEnclosingMethodInfo: AttributeEnclosingMethodInfo
) : JvmEnclosingMethodAttribute {
    override val classIndex: UShort = attributeEnclosingMethodInfo.classIndex
    override val methodIndex: UShort = attributeEnclosingMethodInfo.methodIndex

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeEnclosingMethodInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeEnclosingMethodInfo.info
    }

    override fun className(): String {
        return jvmClass.lookupClassName(classIndex)
    }
}
