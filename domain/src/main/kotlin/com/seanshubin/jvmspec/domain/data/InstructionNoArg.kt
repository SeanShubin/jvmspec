package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionNoArg(
    override val opcode: OpCode
) : Instruction {
    override fun complexity(): Int = 0

    companion object {
        val OPERAND_TYPE = OperandType.NONE
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            return InstructionNoArg(opCode)
        }
    }
}
