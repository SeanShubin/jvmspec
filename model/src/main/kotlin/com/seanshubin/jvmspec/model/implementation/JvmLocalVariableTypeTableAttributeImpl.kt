package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.AttributeLocalVariableTypeTableInfo
import com.seanshubin.jvmspec.classfile.structure.LocalVariableTypeTableEntry
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmLocalVariableTypeTableAttribute

class JvmLocalVariableTypeTableAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeLocalVariableTypeTableInfo: AttributeLocalVariableTypeTableInfo
) : JvmLocalVariableTypeTableAttribute {
    override val localVariableTypeTableLength: UShort = attributeLocalVariableTypeTableInfo.localVariableTypeTableLength
    override val localVariableTypeTable: List<LocalVariableTypeTableEntry> =
        attributeLocalVariableTypeTableInfo.localVariableTypeTable

    override fun name(): String {
        return jvmClass.lookupUtf8(attributeLocalVariableTypeTableInfo.attributeIndex)
    }

    override fun bytes(): List<Byte> {
        return attributeLocalVariableTypeTableInfo.info
    }
}
