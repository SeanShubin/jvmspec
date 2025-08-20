package com.seanshubin.jvmspec.domain.data

class InstructionNoArg(
    override val opcode:OpCode
) :Instruction {
    companion object {
        val OPERAND_TYPE = OperandType.NONE
        fun fromCodeCursor(opCode:OpCode, cursor: CodeCursor): Pair<Instruction, CodeCursor> {
            val instruction = InstructionNoArg(opCode)
            val newCursor = cursor.advance(1)
            return Pair(instruction, newCursor)
        }
    }
}
