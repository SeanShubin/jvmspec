package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributePermittedSubclassesInfo
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmPermittedSubclassesAttribute

class JvmPermittedSubclassesAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributePermittedSubclassesInfo: AttributePermittedSubclassesInfo
) : JvmPermittedSubclassesAttribute {
    override val numberOfClasses: UShort = attributePermittedSubclassesInfo.numberOfClasses
    override val classes: List<UShort> = attributePermittedSubclassesInfo.classes

    override fun name(): String {
        return jvmClass.lookupUtf8(attributePermittedSubclassesInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributePermittedSubclassesInfo.info
    }

    override fun classNames(): List<String> {
        return classes.map { classIndex ->
            jvmClass.lookupClassName(classIndex)
        }
    }
}
