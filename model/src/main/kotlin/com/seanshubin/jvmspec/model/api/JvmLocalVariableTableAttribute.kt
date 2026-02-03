package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.LocalVariableTableEntry

interface JvmLocalVariableTableAttribute : JvmAttribute {
    val localVariableTableLength: UShort
    val localVariableTable: List<LocalVariableTableEntry>
}
