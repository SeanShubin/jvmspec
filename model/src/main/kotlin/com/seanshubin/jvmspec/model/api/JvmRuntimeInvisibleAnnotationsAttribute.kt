package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.AnnotationStructure.Annotation

interface JvmRuntimeInvisibleAnnotationsAttribute : JvmAttribute {
    val numAnnotations: UShort
    val annotations: List<Annotation>
}
