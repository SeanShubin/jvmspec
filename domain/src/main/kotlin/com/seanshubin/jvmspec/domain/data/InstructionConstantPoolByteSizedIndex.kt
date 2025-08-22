package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionConstantPoolByteSizedIndex(
    override val opcode: OpCode,
    val constantPoolIndex: UByte
) : Instruction {
    override fun line(constantPoolLookup: ConstantPoolLookup): String {
        return "${opcode.line} ${constantPoolLookup.line(constantPoolIndex.toUShort())}"
    }

    companion object {
        val OPERAND_TYPE = OperandType.CONSTANT_POOL_BYTE_SIZED_INDEX
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val constantPoolIndex = input.readUnsignedByte().toUByte()
            val instruction = InstructionConstantPoolByteSizedIndex(opCode, constantPoolIndex)
            return instruction
        }
    }
}
