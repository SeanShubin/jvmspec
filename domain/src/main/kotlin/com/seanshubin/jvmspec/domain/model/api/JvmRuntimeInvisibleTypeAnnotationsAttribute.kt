package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.TypeAnnotation

interface JvmRuntimeInvisibleTypeAnnotationsAttribute : JvmAttribute {
    val numAnnotations: UShort
    val annotations: List<TypeAnnotation>
}
