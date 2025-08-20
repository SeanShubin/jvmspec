package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toHex

data class InstructionAndBytes(val instruction: Instruction, val bytes: List<Byte>) {
    fun lines(): List<String> = listOf(bytes.toHex(), instruction.line())
}
