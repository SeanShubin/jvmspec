package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.indent
import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import java.io.DataInput

data class ExceptionTable(
    val startProgramCounter: UShort,
    val endProgramCounter: UShort,
    val handlerProgramCounter: UShort,
    val catchType: UShort
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
        fun fromDataInput(input: DataInput): ExceptionTable {
            val startProgramCounter = input.readUnsignedShort().toUShort()
            val endProgramCounter = input.readUnsignedShort().toUShort()
            val handlerProgramCounter = input.readUnsignedShort().toUShort()
            val catchType = input.readUnsignedShort().toUShort()
            return ExceptionTable(
                startProgramCounter,
                endProgramCounter,
                handlerProgramCounter,
                catchType
            )
        }
    }
}
