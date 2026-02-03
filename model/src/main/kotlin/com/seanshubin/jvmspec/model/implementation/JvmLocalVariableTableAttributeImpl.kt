package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeLocalVariableTableInfo
import com.seanshubin.jvmspec.classfile.structure.LocalVariableTableEntry
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmLocalVariableTableAttribute

class JvmLocalVariableTableAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeLocalVariableTableInfo: AttributeLocalVariableTableInfo
) : JvmLocalVariableTableAttribute {
    override val localVariableTableLength: UShort = attributeLocalVariableTableInfo.localVariableTableLength
    override val localVariableTable: List<LocalVariableTableEntry> = attributeLocalVariableTableInfo.localVariableTable

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeLocalVariableTableInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeLocalVariableTableInfo.info
    }
}
