package com.seanshubin.jvmspec.domain.model.api

interface JvmNestMembersAttribute : JvmAttribute {
    val numberOfClasses: UShort
    val classes: List<UShort>
    fun classNames(): List<String>
}
