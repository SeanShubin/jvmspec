package com.seanshubin.jvmspec.domain.jvm

interface JvmInstruction {
    fun name(): String
    fun code(): UByte
    fun args(): List<JvmArgument>
}
