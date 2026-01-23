package com.seanshubin.jvmspec.domain.model.api

interface JvmExceptionsAttribute : JvmAttribute {
    fun exceptionClassNames(): List<String>
    val numberOfExceptions: UShort
    val exceptionIndexTable: List<UShort>
}
