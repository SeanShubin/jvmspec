package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.MethodParameter

interface JvmMethodParametersAttribute : JvmAttribute {
    val parametersCount: UByte
    val parameters: List<MethodParameter>
}
