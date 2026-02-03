package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.AnnotationStructure.Annotation

interface JvmRuntimeVisibleAnnotationsAttribute : JvmAttribute {
    val numAnnotations: UShort
    val annotations: List<Annotation>
}
