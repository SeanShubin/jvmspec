package com.seanshubin.jvmspec.domain.api

interface ApiCodeAttribute {
    fun complexity(): Int
    fun opcodes(): List<String>
    fun instructions(): List<ApiInstruction>
}
