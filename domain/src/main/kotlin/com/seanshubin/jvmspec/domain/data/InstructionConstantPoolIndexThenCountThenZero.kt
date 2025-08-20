package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionConstantPoolIndexThenCountThenZero(
    override val opcode: OpCode,
    val constantPoolIndex: Int,
    val count: Int
) : Instruction {
    override fun line(): String {
        return "${opcode.line} $constantPoolIndex $count 0"
    }

    companion object {
        val OPERAND_TYPE = OperandType.CONSTANT_POOL_INDEX_THEN_COUNT_THEN_ZERO
        fun fromDataInput(opCode: OpCode, dataInput: DataInput, index: Int): Instruction {
            val constantPoolIndex = dataInput.readUnsignedShort()
            val count = dataInput.readUnsignedByte()
            val zero = dataInput.readByte()
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
