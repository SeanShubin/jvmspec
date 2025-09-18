package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import java.io.DataInput

class InstructionByte(
    override val opcode: OpCode,
    val value: Byte
) : Instruction {
    override fun line(constantPoolLookup: ConstantPoolLookup): String {
        return "${opcode.line} ${value.toDecHex()}"
    }

    override fun cyclomaticComplexity(): Int = 0

    companion object {
        val OPERAND_TYPE = OperandType.BYTE
        fun fromDataInput(opCode: OpCode, input: DataInput, codeIndex: Int): Instruction {
            val value = input.readByte()
            val instruction = InstructionByte(opCode, value)
            return instruction
        }
    }
}
