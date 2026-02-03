package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.AnnotationStructure.ElementValue

interface JvmAnnotationDefaultAttribute : JvmAttribute {
    val defaultValue: ElementValue
}
