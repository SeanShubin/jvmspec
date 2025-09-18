package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import java.io.DataInput

class InstructionShort(
    override val opcode: OpCode,
    val value: Short
) : Instruction {
    override fun line(constantPoolLookup: ConstantPoolLookup): String {
        return "${opcode.line} ${value.toDecHex()}"
    }

    override fun cyclomaticComplexity(): Int = 0

    companion object {
        val OPERAND_TYPE = OperandType.SHORT
        fun fromDataInput(opCode: OpCode, input: DataInput, codeIndex: Int): Instruction {
            val value = input.readShort()
            val instruction = InstructionShort(opCode, value)
            return instruction
        }
    }
}
