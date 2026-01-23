package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.InnerClassInfo

interface JvmInnerClassesAttribute : JvmAttribute {
    val numberOfClasses: UShort
    val classes: List<InnerClassInfo>
}
