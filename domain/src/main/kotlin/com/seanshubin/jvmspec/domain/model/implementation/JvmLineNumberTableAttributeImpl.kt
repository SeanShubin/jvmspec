package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.AttributeLineNumberTableInfo
import com.seanshubin.jvmspec.domain.classfile.structure.LineNumberTableEntry
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmLineNumberTableAttribute

class JvmLineNumberTableAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeLineNumberTableInfo: AttributeLineNumberTableInfo
) : JvmLineNumberTableAttribute {
    override val lineNumberTableLength: UShort = attributeLineNumberTableInfo.lineNumberTableLength
    override val lineNumberTable: List<LineNumberTableEntry> = attributeLineNumberTableInfo.lineNumberTable

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeLineNumberTableInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeLineNumberTableInfo.info
    }
}
