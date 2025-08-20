package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionConstantPoolIndexThenDimensions(
    override val opcode: OpCode,
    val classIndex: Int,
    val dimensions: Int
) : Instruction {
    override fun line(): String {
        return "${opcode.formatted} $classIndex $dimensions"
    }

    companion object {
        val OPERAND_TYPE = OperandType.CONSTANT_POOL_INDEX_THEN_DIMENSIONS
        fun fromDataInput(opCode: OpCode, dataInput: DataInput, index: Int): Instruction {
            val classIndex = dataInput.readUnsignedShort()
            val dimensions = dataInput.readUnsignedByte()
            val instruction = InstructionConstantPoolIndexThenDimensions(opCode, classIndex, dimensions)
            return instruction
        }
    }
}
