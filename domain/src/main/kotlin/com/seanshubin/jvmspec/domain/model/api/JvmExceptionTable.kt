package com.seanshubin.jvmspec.domain.model.api

data class JvmExceptionTable(
    val startProgramCounter: UShort,
    val endProgramCounter: UShort,
    val handlerProgramCounter: UShort,
    val catchType: UShort
)