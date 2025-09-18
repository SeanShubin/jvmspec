package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionConstantPoolIndex(
    override val opcode: OpCode,
    val constantPoolIndex: UShort
) : Instruction {
    override fun line(constantPoolLookup: ConstantPoolLookup): String {
        return "${opcode.line} ${constantPoolLookup.line(constantPoolIndex)}"
    }

    override fun cyclomaticComplexity(): Int = 0

    companion object {
        val OPERAND_TYPE = OperandType.CONSTANT_POOL_INDEX
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val constantPoolIndex = input.readUnsignedShort().toUShort()
            val instruction = InstructionConstantPoolIndex(opCode, constantPoolIndex)
            return instruction
        }
    }
}
