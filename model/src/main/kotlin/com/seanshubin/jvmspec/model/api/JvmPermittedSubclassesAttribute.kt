package com.seanshubin.jvmspec.model.api

interface JvmPermittedSubclassesAttribute : JvmAttribute {
    val numberOfClasses: UShort
    val classes: List<UShort>
    fun classNames(): List<String>
}
