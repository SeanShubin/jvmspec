package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.indent
import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import java.io.DataInput

data class ExceptionTable(
    val startProgramCounter: Short,
    val endProgramCounter: Short,
    val handlerProgramCounter: Short,
    val catchType: Short
) {
    fun lines(index: Int): List<String> {
        val header = listOf("ExceptionTable[$index]")
        val content = listOf(
            "startProgramCounter=${startProgramCounter.toDecHex()}",
            "endProgramCounter=${endProgramCounter.toDecHex()}",
            "handlerProgramCounter=${handlerProgramCounter.toDecHex()}",
            "catchType=${catchType.toDecHex()}"
        ).map(indent)
        return header + content
    }
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
