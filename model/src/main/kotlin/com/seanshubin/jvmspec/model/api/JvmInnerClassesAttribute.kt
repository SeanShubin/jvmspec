package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.InnerClassInfo

interface JvmInnerClassesAttribute : JvmAttribute {
    val numberOfClasses: UShort
    val classes: List<InnerClassInfo>
}
