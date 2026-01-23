package com.seanshubin.jvmspec.domain.model.api

interface JvmSourceFileAttribute : JvmAttribute {
    fun sourceFileName(): String
    val sourceFileIndex: UShort
}
