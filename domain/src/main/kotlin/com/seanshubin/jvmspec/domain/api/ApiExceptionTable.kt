package com.seanshubin.jvmspec.domain.api

data class ApiExceptionTable(
    val startProgramCounter: UShort,
    val endProgramCounter: UShort,
    val handlerProgramCounter: UShort,
    val catchType: UShort
)