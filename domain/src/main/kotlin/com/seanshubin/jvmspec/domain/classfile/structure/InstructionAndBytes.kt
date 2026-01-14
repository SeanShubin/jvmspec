package com.seanshubin.jvmspec.domain.classfile.structure

data class InstructionAndBytes(
    val instruction: Instruction,
    val bytes: List<Byte>
)