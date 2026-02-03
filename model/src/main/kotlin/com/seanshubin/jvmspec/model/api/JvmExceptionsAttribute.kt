package com.seanshubin.jvmspec.model.api

interface JvmExceptionsAttribute : JvmAttribute {
    fun exceptionClassNames(): List<String>
    val numberOfExceptions: UShort
    val exceptionIndexTable: List<UShort>
}
