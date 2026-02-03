package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.classfile.descriptor.Signature

data class InvocationAnalysis(
    val className: String,
    val methodName: String,
    val signature: Signature,
    val invocationOpcodeName: String,
    val invocationType: InvocationType
)
