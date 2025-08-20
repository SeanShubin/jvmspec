package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionArrayType(
    override val opcode: OpCode,
    val arrayType: ArrayType
) : Instruction {
    override fun line(): String {
        return "${opcode.formatted} ${arrayType.line()}"
    }

    companion object {
        val OPERAND_TYPE = OperandType.ARRAY_TYPE
        fun fromDataInput(opCode: OpCode, dataInput: DataInput, codeIndex: Int): Instruction {
            val arrayTypeValue = dataInput.readByte()
            val arrayType = ArrayType.fromByte(arrayTypeValue)
            val instruction = InstructionArrayType(opCode, arrayType)
            return instruction
        }
    }
}
