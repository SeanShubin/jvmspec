package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.LocalVariableTypeTableEntry

interface JvmLocalVariableTypeTableAttribute : JvmAttribute {
    val localVariableTypeTableLength: UShort
    val localVariableTypeTable: List<LocalVariableTypeTableEntry>
}
