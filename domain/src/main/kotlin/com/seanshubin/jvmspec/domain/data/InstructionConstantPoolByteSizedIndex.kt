package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionConstantPoolByteSizedIndex(
    override val opcode:OpCode,
    val constantPoolIndex: Int
) :Instruction {
    override fun line(): String {
        return "${opcode.formatted} $constantPoolIndex"
    }

    companion object {
        val OPERAND_TYPE = OperandType.CONSTANT_POOL_BYTE_SIZED_INDEX
        fun fromDataInput(opCode:OpCode, dataInput: DataInput, index:Int): Instruction{
            val constantPoolIndex = dataInput.readUnsignedByte()
            val instruction = InstructionConstantPoolByteSizedIndex(opCode, constantPoolIndex)
            return instruction
        }
    }
}
