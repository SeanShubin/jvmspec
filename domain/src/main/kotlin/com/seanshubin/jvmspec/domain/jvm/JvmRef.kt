package com.seanshubin.jvmspec.domain.jvm

import com.seanshubin.jvmspec.domain.descriptor.Signature

data class JvmRef(
    val className: String,
    val name: String,
    val signature: Signature
)
