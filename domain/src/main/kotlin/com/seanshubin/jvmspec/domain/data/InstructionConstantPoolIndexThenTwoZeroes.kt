package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionConstantPoolIndexThenTwoZeroes(
    override val opcode: OpCode,
    val constantPoolIndex: Int
) : Instruction {
    override fun line(): String {
        return "${opcode.formatted} $constantPoolIndex 0 0"
    }

    companion object {
        val OPERAND_TYPE = OperandType.CONSTANT_POOL_INDEX_THEN_TWO_ZEROES
        fun fromDataInput(opCode: OpCode, dataInput: DataInput, index: Int): Instruction {
            val constantPoolIndex = dataInput.readUnsignedShort()
            val firstZero = dataInput.readByte()
            if (firstZero != 0.toByte()) {
                throw IllegalArgumentException("Expected first zero byte, but found $firstZero")
            }
            val secondZero = dataInput.readByte()
            if (secondZero != 0.toByte()) {
                throw IllegalArgumentException("Expected second zero byte, but found $secondZero")
            }
            val instruction = InstructionConstantPoolIndexThenTwoZeroes(opCode, constantPoolIndex)
            return instruction
        }
    }
}
