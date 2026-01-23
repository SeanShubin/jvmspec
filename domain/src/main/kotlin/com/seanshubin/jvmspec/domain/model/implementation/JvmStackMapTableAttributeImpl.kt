package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeStackMapTableInfo
import com.seanshubin.jvmspec.domain.classfile.structure.StackMapFrame
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmStackMapTableAttribute

class JvmStackMapTableAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeStackMapTableInfo: AttributeStackMapTableInfo
) : JvmStackMapTableAttribute {
    override val numberOfEntries: UShort = attributeStackMapTableInfo.numberOfEntries
    override val entries: List<StackMapFrame> = attributeStackMapTableInfo.entries

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeStackMapTableInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeStackMapTableInfo.info
    }
}
