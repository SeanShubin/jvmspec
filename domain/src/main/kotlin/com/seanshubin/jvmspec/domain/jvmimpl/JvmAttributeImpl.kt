package com.seanshubin.jvmspec.domain.jvmimpl

import com.seanshubin.jvmspec.domain.data.AttributeInfo
import com.seanshubin.jvmspec.domain.jvm.JvmAttribute
import com.seanshubin.jvmspec.domain.jvm.JvmClass

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
