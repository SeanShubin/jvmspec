package com.seanshubin.jvmspec.domain.jvm

interface JvmMethod : JvmFieldOrMethod {
    fun code(): JvmCodeAttribute?
    fun complexity(): Int
    fun instructions(): List<JvmInstruction>
}
