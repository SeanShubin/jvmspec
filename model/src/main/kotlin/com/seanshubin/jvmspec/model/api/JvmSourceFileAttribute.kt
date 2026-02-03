package com.seanshubin.jvmspec.model.api

interface JvmSourceFileAttribute : JvmAttribute {
    fun sourceFileName(): String
    val sourceFileIndex: UShort
}
