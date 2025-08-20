package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionIndexConst(
    override val opcode: OpCode,
    val index: Int,
    val const: Byte
) : Instruction {
    override fun line(): String {
        return "${opcode.formatted} $index $const"
    }

    companion object {
        val OPERAND_TYPE = OperandType.INDEX_CONST
        fun fromDataInput(opCode: OpCode, dataInput: DataInput, codeIndex: Int): Instruction {
            val index = dataInput.readUnsignedByte()
            val const = dataInput.readByte()
            val instruction = InstructionIndexConst(opCode, index, const)
            return instruction
        }
    }
}
