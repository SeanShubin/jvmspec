package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.descriptor.Signature

data class JvmRef(
    val className: String,
    val name: String,
    val signature: Signature
)
