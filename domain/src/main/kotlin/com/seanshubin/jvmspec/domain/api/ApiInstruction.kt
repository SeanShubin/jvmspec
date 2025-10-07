package com.seanshubin.jvmspec.domain.api

interface ApiInstruction {
    fun opcode(): String
    fun line(): String
    fun arg1Ref(): ApiRef
    fun arg1ClassName(): String
}
