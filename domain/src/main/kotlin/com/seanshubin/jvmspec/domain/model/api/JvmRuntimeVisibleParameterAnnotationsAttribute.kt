package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.ParameterAnnotation

interface JvmRuntimeVisibleParameterAnnotationsAttribute : JvmAttribute {
    val numParameters: UByte
    val parameterAnnotations: List<ParameterAnnotation>
}
