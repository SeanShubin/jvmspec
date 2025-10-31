package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionIndexConst(
    override val opcode: OpCode,
    val index: Int,
    val const: Byte
) : Instruction {
    override fun cyclomaticComplexity(): Int = 0

    companion object {
        val OPERAND_TYPE = OperandType.INDEX_CONST
        fun fromDataInput(opCode: OpCode, input: DataInput, codeIndex: Int): Instruction {
            val index = input.readUnsignedByte()
            val const = input.readByte()
            val instruction = InstructionIndexConst(opCode, index, const)
            return instruction
        }
    }
}
