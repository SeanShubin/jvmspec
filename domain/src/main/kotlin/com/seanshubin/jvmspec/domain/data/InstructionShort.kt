package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionShort(
    override val opcode:OpCode,
    val value: Short
) :Instruction {
    override fun line(): String {
        return "${opcode.formatted} $value"
    }
    companion object {
        val OPERAND_TYPE = OperandType.SHORT
        fun fromDataInput(opCode:OpCode, dataInput: DataInput, codeIndex:Int): Instruction{
            val value = dataInput.readShort()
            val instruction = InstructionShort(opCode, value)
            return instruction
        }
    }
}
