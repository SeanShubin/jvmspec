package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.ParameterAnnotation

interface JvmRuntimeInvisibleParameterAnnotationsAttribute : JvmAttribute {
    val numParameters: UByte
    val parameterAnnotations: List<ParameterAnnotation>
}
