package com.seanshubin.jvmspec.classfile.structure

import com.seanshubin.jvmspec.classfile.specification.ArrayType
import java.io.DataInput

class InstructionArrayType(
    override val opcode: OpCode,
    val arrayType: ArrayType
) : Instruction {
    override fun complexity(): Int = 0

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
