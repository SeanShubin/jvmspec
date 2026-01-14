package com.seanshubin.jvmspec.domain.model.api

interface JvmCodeAttribute : JvmAttribute {
    fun complexity(): Int
    fun instructions(): List<JvmInstruction>
    fun exceptionTable(): List<JvmExceptionTable>
    fun attributes(): List<JvmAttribute>
    val maxStack: UShort
    val maxLocals: UShort
    val codeLength: Int
}
