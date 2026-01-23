package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.ParameterAnnotation

interface JvmRuntimeInvisibleParameterAnnotationsAttribute : JvmAttribute {
    val numParameters: UByte
    val parameterAnnotations: List<ParameterAnnotation>
}
