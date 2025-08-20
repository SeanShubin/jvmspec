package com.seanshubin.jvmspec.domain.data

data class ExceptionTable(
    val startProgramCounter: Short,
    val endProgramCounter: Short,
    val handlerProgramCounter: Short,
    val catchType: Short
) {
}