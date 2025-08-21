package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import java.io.DataInput

class InstructionConstantPoolIndexThenDimensions(
    override val opcode: OpCode,
    val classIndex: Int,
    val dimensions: Int
) : Instruction {
    override fun line(): String {
        return "${opcode.line} ${classIndex.toDecHex()} ${dimensions.toDecHex()}"
    }

    companion object {
        val OPERAND_TYPE = OperandType.CONSTANT_POOL_INDEX_THEN_DIMENSIONS
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val classIndex = input.readUnsignedShort()
            val dimensions = input.readUnsignedByte()
            val instruction = InstructionConstantPoolIndexThenDimensions(opCode, classIndex, dimensions)
            return instruction
        }
    }
}
