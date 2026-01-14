package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

class InstructionConstantPoolIndexThenDimensions(
    override val opcode: OpCode,
    val classIndex: UShort,
    val dimensions: Int
) : Instruction {
    override fun complexity(): Int = 0

    companion object {
        val OPERAND_TYPE = OperandType.CONSTANT_POOL_INDEX_THEN_DIMENSIONS
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val classIndex = input.readUnsignedShort().toUShort()
            val dimensions = input.readUnsignedByte()
            val instruction = InstructionConstantPoolIndexThenDimensions(opCode, classIndex, dimensions)
            return instruction
        }
    }
}
