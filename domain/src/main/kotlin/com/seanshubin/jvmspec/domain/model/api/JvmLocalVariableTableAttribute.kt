package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.LocalVariableTableEntry

interface JvmLocalVariableTableAttribute : JvmAttribute {
    val localVariableTableLength: UShort
    val localVariableTable: List<LocalVariableTableEntry>
}
