package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.AnnotationStructure.Annotation

interface JvmRuntimeVisibleAnnotationsAttribute : JvmAttribute {
    val numAnnotations: UShort
    val annotations: List<Annotation>
}
