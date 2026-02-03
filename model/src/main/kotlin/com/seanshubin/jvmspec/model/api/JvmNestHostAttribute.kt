package com.seanshubin.jvmspec.model.api

interface JvmNestHostAttribute : JvmAttribute {
    val hostClassIndex: UShort
    fun hostClassName(): String
}
