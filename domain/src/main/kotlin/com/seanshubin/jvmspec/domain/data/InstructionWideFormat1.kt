package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionWideFormat1(
    override val opcode: OpCode,
    val modifiedOpCode: OpCode,
    val localVariableIndex: UShort
) : Instruction {
    override fun cyclomaticComplexity(): Int = 0

    companion object {
        fun fromDataInput(opCode: OpCode, modifiedOpCode: OpCode, input: DataInput, index: Int): Instruction {
            val localVariableIndex = input.readUnsignedShort().toUShort()
            return InstructionWideFormat1(
                opcode = opCode,
                modifiedOpCode = modifiedOpCode,
                localVariableIndex = localVariableIndex
            )
        }
    }
}
