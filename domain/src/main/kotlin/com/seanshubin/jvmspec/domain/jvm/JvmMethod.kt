package com.seanshubin.jvmspec.domain.jvm

interface JvmMethod : JvmFieldOrMethod {
    fun code(): JvmCodeAttribute?
    fun complexity(): Int

    fun javaSignature(): String

    fun instructions(): List<JvmInstruction>
}
