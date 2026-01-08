package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.domain.descriptor.Signature

data class InvocationAnalysis(
    val className: String,
    val methodName: String,
    val signature: Signature,
    val invocationOpcodeName: String,
    val invocationType: InvocationType
)
