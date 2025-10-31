package com.seanshubin.jvmspec.domain.jvm

interface JvmCodeAttribute : JvmAttribute {
    fun complexity(): Int
    fun opcodes(): List<String>
    fun instructions(): List<JvmInstruction>
    fun exceptionTable(): List<JvmExceptionTable>
    fun attributes(): List<JvmAttribute>
}
