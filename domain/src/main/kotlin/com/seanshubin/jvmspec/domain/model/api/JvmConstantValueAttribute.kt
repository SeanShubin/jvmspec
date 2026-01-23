package com.seanshubin.jvmspec.domain.model.api

interface JvmConstantValueAttribute : JvmAttribute {
    fun constantValue(): String
    val constantValueIndex: UShort
}
