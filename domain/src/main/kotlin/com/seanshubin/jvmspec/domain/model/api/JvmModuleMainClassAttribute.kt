package com.seanshubin.jvmspec.domain.model.api

interface JvmModuleMainClassAttribute : JvmAttribute {
    val mainClassIndex: UShort
    fun mainClassName(): String
}
