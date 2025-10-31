package com.seanshubin.jvmspec.domain.jvm

data class JvmExceptionTable(
    val startProgramCounter: UShort,
    val endProgramCounter: UShort,
    val handlerProgramCounter: UShort,
    val catchType: UShort
)