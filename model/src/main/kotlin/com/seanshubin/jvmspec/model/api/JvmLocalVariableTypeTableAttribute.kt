package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.LocalVariableTypeTableEntry

interface JvmLocalVariableTypeTableAttribute : JvmAttribute {
    val localVariableTypeTableLength: UShort
    val localVariableTypeTable: List<LocalVariableTypeTableEntry>
}
