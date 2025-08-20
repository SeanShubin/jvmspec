package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionByte(
    override val opcode: OpCode,
    val value: Byte
) : Instruction {
    override fun line(): String {
        return "${opcode.line} $value"
    }

    companion object {
        val OPERAND_TYPE = OperandType.BYTE
        fun fromDataInput(opCode: OpCode, dataInput: DataInput, codeIndex: Int): Instruction {
            val value = dataInput.readByte()
            val instruction = InstructionByte(opCode, value)
            return instruction
        }
    }
}
