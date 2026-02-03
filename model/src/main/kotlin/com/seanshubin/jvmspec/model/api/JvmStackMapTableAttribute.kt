package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.StackMapFrame

interface JvmStackMapTableAttribute : JvmAttribute {
    val numberOfEntries: UShort
    val entries: List<StackMapFrame>
}
