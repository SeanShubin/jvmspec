package com.seanshubin.jvmspec.model.api

interface JvmConstantValueAttribute : JvmAttribute {
    fun constantValue(): String
    val constantValueIndex: UShort
}
