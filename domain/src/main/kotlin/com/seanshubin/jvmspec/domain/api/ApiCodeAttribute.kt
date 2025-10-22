package com.seanshubin.jvmspec.domain.api

interface ApiCodeAttribute : ApiAttribute {
    fun complexity(): Int
    fun opcodes(): List<String>
    fun instructions(): List<ApiInstruction>
    fun exceptionTable(): List<ApiExceptionTable>
    fun attributes(): List<ApiAttribute>
}
