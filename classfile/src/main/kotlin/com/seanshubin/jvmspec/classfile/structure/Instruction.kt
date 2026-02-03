package com.seanshubin.jvmspec.classfile.structure

interface Instruction {
    val opcode: OpCode
    fun complexity(): Int
}
