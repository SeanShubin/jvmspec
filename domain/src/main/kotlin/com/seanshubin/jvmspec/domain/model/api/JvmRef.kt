package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.descriptor.Signature

data class JvmRef(
    val className: String,
    val name: String,
    val signature: Signature
)
