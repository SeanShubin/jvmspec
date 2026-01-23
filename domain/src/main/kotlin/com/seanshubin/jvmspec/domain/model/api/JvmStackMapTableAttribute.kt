package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.StackMapFrame

interface JvmStackMapTableAttribute : JvmAttribute {
    val numberOfEntries: UShort
    val entries: List<StackMapFrame>
}
