package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionArrayType(
    override val opcode: OpCode,
    val arrayType: ArrayType
) : Instruction {
    override fun line(constantPoolLookup: ConstantPoolLookup): String {
        return "${opcode.line} ${arrayType.line()}"
    }

    override fun cyclomaticComplexity(): Int = 0

    companion object {
        val OPERAND_TYPE = OperandType.ARRAY_TYPE
        fun fromDataInput(opCode: OpCode, input: DataInput, codeIndex: Int): Instruction {
            val arrayTypeValue = input.readByte()
            val arrayType = ArrayType.fromByte(arrayTypeValue)
            val instruction = InstructionArrayType(opCode, arrayType)
            return instruction
        }
    }
}
