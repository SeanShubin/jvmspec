package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.LineNumberTableEntry

interface JvmLineNumberTableAttribute : JvmAttribute {
    val lineNumberTableLength: UShort
    val lineNumberTable: List<LineNumberTableEntry>
}
