package com.seanshubin.jvmspec.domain.data

interface Instruction {
    val opcode: OpCode
    fun line(constantPoolLookup: ConstantPoolLookup): String
}
