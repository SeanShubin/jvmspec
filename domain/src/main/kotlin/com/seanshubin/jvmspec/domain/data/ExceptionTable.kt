package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

data class ExceptionTable(
    val startProgramCounter: Short,
    val endProgramCounter: Short,
    val handlerProgramCounter: Short,
    val catchType: Short
) {
    companion object {
        fun fromDataInput(dataInput: DataInput): ExceptionTable {
            val startProgramCounter = dataInput.readShort()
            val endProgramCounter = dataInput.readShort()
            val handlerProgramCounter = dataInput.readShort()
            val catchType = dataInput.readShort()
            return ExceptionTable(
                startProgramCounter,
                endProgramCounter,
                handlerProgramCounter,
                catchType
            )
        }
    }
}
