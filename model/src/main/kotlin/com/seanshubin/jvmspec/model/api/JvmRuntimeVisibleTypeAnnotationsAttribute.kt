package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.TypeAnnotation

interface JvmRuntimeVisibleTypeAnnotationsAttribute : JvmAttribute {
    val numAnnotations: UShort
    val annotations: List<TypeAnnotation>
}
