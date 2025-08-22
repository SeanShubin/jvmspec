package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionConstantPoolIndexThenTwoZeroes(
    override val opcode: OpCode,
    val constantPoolIndex: UShort
) : Instruction {
    override fun line(constantPoolLookup: ConstantPoolLookup): String {
        return "${opcode.line} ${constantPoolLookup.line(constantPoolIndex)} 0 0"
    }

    companion object {
        val OPERAND_TYPE = OperandType.CONSTANT_POOL_INDEX_THEN_TWO_ZEROES
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val constantPoolIndex = input.readUnsignedShort().toUShort()
            val firstZero = input.readByte()
            if (firstZero != 0.toByte()) {
                throw IllegalArgumentException("Expected first zero byte, but found $firstZero")
            }
            val secondZero = input.readByte()
            if (secondZero != 0.toByte()) {
                throw IllegalArgumentException("Expected second zero byte, but found $secondZero")
            }
            val instruction = InstructionConstantPoolIndexThenTwoZeroes(opCode, constantPoolIndex)
            return instruction
        }
    }
}
