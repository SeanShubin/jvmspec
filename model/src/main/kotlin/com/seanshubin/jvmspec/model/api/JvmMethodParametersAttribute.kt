package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.MethodParameter

interface JvmMethodParametersAttribute : JvmAttribute {
    val parametersCount: UByte
    val parameters: List<MethodParameter>
}
