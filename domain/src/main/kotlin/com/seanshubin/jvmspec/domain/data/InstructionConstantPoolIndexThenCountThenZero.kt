package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionConstantPoolIndexThenCountThenZero(
    override val opcode: OpCode,
    val constantPoolIndex: UShort,
    val count: Int
) : Instruction {
    override fun cyclomaticComplexity(): Int = 0

    companion object {
        val OPERAND_TYPE = OperandType.CONSTANT_POOL_INDEX_THEN_COUNT_THEN_ZERO
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val constantPoolIndex = input.readUnsignedShort().toUShort()
            val count = input.readUnsignedByte()
            val zero = input.readByte()
            if (zero != 0.toByte()) {
                throw IllegalArgumentException("Expected zero but found $zero for opcode $opCode")
            }
            val instruction = InstructionConstantPoolIndexThenCountThenZero(
                opCode,
                constantPoolIndex,
                count
            )
            return instruction
        }
    }
}
