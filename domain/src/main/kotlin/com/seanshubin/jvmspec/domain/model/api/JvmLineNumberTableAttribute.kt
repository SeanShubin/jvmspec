package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.LineNumberTableEntry

interface JvmLineNumberTableAttribute : JvmAttribute {
    val lineNumberTableLength: UShort
    val lineNumberTable: List<LineNumberTableEntry>
}
