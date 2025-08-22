package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toHex

data class InstructionAndBytes(val instruction: Instruction, val bytes: List<Byte>) {
    fun lines(constantPoolLookup: ConstantPoolLookup): List<String> =
        listOf(bytes.toHex(), instruction.line(constantPoolLookup))

    fun line(constantPoolLookup: ConstantPoolLookup): String = instruction.line(constantPoolLookup)
}
