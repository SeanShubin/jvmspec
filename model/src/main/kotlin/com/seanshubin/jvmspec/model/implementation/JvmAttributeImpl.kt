package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeInfo
import com.seanshubin.jvmspec.model.api.JvmAttribute
import com.seanshubin.jvmspec.model.api.JvmClass

class JvmAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeInfo: AttributeInfo
) : JvmAttribute {
    override fun name(): String {
        val nameIndex = attributeInfo.attributeIndex
        return jvmClass.lookupUtf8(nameIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeInfo.info
    }
}
