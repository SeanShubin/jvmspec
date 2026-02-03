package com.seanshubin.jvmspec.model.api

interface JvmMethod : JvmFieldOrMethod {
    fun code(): JvmCodeAttribute?
    fun complexity(): Int
    fun instructions(): List<JvmInstruction>
}
