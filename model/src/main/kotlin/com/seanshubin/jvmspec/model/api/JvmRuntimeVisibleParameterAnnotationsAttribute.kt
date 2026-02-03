package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.ParameterAnnotation

interface JvmRuntimeVisibleParameterAnnotationsAttribute : JvmAttribute {
    val numParameters: UByte
    val parameterAnnotations: List<ParameterAnnotation>
}
