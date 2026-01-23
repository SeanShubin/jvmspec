package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeInnerClassesInfo
import com.seanshubin.jvmspec.domain.classfile.structure.InnerClassInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmInnerClassesAttribute

class JvmInnerClassesAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeInnerClassesInfo: AttributeInnerClassesInfo
) : JvmInnerClassesAttribute {
    override val numberOfClasses: UShort = attributeInnerClassesInfo.numberOfClasses
    override val classes: List<InnerClassInfo> = attributeInnerClassesInfo.classes

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeInnerClassesInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeInnerClassesInfo.info
    }
}
