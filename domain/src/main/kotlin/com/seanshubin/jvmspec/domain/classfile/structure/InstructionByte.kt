package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

class InstructionByte(
    override val opcode: OpCode,
    val value: Byte
) : Instruction {
    override fun complexity(): Int = 0

    companion object {
        val OPERAND_TYPE = OperandType.BYTE
        fun fromDataInput(opCode: OpCode, input: DataInput, codeIndex: Int): Instruction {
            val value = input.readByte()
            val instruction = InstructionByte(opCode, value)
            return instruction
        }
    }
}
