package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeLineNumberTableInfo
import com.seanshubin.jvmspec.classfile.structure.LineNumberTableEntry
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmLineNumberTableAttribute

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
