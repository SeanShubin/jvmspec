package com.seanshubin.jvmspec.model.api

interface JvmModuleMainClassAttribute : JvmAttribute {
    val mainClassIndex: UShort
    fun mainClassName(): String
}
