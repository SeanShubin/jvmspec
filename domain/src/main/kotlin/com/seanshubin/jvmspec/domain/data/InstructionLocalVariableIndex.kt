package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionLocalVariableIndex(
    override val opcode: OpCode,
    val localVariableIndex: Int
) : Instruction {
    override fun line(): String {
        return "${opcode.formatted} $localVariableIndex"
    }

    companion object {
        val OPERAND_TYPE = OperandType.LOCAL_VARIABLE_INDEX
        fun fromDataInput(opCode: OpCode, dataInput: DataInput, index: Int): Instruction {
            val localVariableIndex = dataInput.readUnsignedByte()
            val instruction = InstructionLocalVariableIndex(opCode, localVariableIndex)
            return instruction
        }
    }
}
