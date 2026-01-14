package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

class InstructionShort(
    override val opcode: OpCode,
    val value: Short
) : Instruction {
    override fun complexity(): Int = 0

    companion object {
        val OPERAND_TYPE = OperandType.SHORT
        fun fromDataInput(opCode: OpCode, input: DataInput, codeIndex: Int): Instruction {
            val value = input.readShort()
            val instruction = InstructionShort(opCode, value)
            return instruction
        }
    }
}
