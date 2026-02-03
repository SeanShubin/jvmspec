package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeNestMembersInfo
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmNestMembersAttribute

class JvmNestMembersAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeNestMembersInfo: AttributeNestMembersInfo
) : JvmNestMembersAttribute {
    override val numberOfClasses: UShort = attributeNestMembersInfo.numberOfClasses
    override val classes: List<UShort> = attributeNestMembersInfo.classes

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeNestMembersInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeNestMembersInfo.info
    }

    override fun classNames(): List<String> {
        return classes.map { classIndex ->
            jvmClass.lookupClassName(classIndex)
        }
    }
}
