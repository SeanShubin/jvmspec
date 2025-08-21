package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import java.io.DataInput

class InstructionConstantPoolIndex(
    override val opcode: OpCode,
    val constantPoolIndex: Int
) : Instruction {
    override fun line(): String {
        return "${opcode.line} ${constantPoolIndex.toDecHex()}"
    }

    companion object {
        val OPERAND_TYPE = OperandType.CONSTANT_POOL_INDEX
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val constantPoolIndex = input.readUnsignedShort()
            val instruction = InstructionConstantPoolIndex(opCode, constantPoolIndex)
            return instruction
        }
    }
}
