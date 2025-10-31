package com.seanshubin.jvmspec.domain.data

data class InstructionAndBytes(
    val instruction: Instruction,
    val bytes: List<Byte>
)