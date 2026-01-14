package com.seanshubin.jvmspec.domain.classfile.structure

interface Instruction {
    val opcode: OpCode
    fun complexity(): Int
}
