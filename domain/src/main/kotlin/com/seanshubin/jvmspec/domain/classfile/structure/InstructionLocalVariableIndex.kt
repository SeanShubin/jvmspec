package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

class InstructionLocalVariableIndex(
    override val opcode: OpCode,
    val localVariableIndex: Int
) : Instruction {
    override fun complexity(): Int = 0

    companion object {
        val OPERAND_TYPE = OperandType.LOCAL_VARIABLE_INDEX
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val localVariableIndex = input.readUnsignedByte()
            val instruction = InstructionLocalVariableIndex(opCode, localVariableIndex)
            return instruction
        }
    }
}
