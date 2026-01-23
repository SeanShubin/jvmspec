package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeExceptionsInfo
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmExceptionsAttribute

class JvmExceptionsAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeExceptionsInfo: AttributeExceptionsInfo
) : JvmExceptionsAttribute {
    override val numberOfExceptions: UShort = attributeExceptionsInfo.numberOfExceptions
    override val exceptionIndexTable: List<UShort> = attributeExceptionsInfo.exceptionIndexTable

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeExceptionsInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeExceptionsInfo.info
    }

    override fun exceptionClassNames(): List<String> {
        return exceptionIndexTable.map { index ->
            jvmClass.lookupClassName(index)
        }
    }
}
