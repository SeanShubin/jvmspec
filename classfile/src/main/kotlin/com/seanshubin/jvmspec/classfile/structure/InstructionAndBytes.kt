package com.seanshubin.jvmspec.classfile.structure

data class InstructionAndBytes(
    val instruction: Instruction,
    val bytes: List<Byte>
)