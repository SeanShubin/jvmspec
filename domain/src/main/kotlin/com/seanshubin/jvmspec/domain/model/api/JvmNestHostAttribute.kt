package com.seanshubin.jvmspec.domain.model.api

interface JvmNestHostAttribute : JvmAttribute {
    val hostClassIndex: UShort
    fun hostClassName(): String
}
