package com.seanshubin.jvmspec.domain.api

interface ApiInstruction {
    fun name(): String
    fun code(): UByte
    fun args(): List<ApiArgument>
}
