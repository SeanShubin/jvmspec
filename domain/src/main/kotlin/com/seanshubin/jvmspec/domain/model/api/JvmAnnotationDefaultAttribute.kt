package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.ElementValue

interface JvmAnnotationDefaultAttribute : JvmAttribute {
    val defaultValue: ElementValue
}
