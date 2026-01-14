package com.seanshubin.jvmspec.domain.model.api

interface JvmInstruction {
    fun name(): String
    fun code(): UByte
    fun bytes(): List<Byte>
    fun args(): List<JvmArgument>
}
