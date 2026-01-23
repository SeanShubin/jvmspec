package com.seanshubin.jvmspec.domain.model.api

interface JvmEnclosingMethodAttribute : JvmAttribute {
    val classIndex: UShort
    val methodIndex: UShort
    fun className(): String
}
