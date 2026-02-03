package com.seanshubin.jvmspec.model.api

interface JvmNestMembersAttribute : JvmAttribute {
    val numberOfClasses: UShort
    val classes: List<UShort>
    fun classNames(): List<String>
}
