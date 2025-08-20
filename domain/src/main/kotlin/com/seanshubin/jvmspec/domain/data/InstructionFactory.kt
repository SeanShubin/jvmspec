package com.seanshubin.jvmspec.domain.data

object InstructionFactory {
    fun allInstructions(code:List<Byte>):List<Instruction>{
        var cursor = CodeCursor(0, code)
        val instructions = mutableListOf<Instruction>()
        while (cursor.hasMore()) {
            val (instruction, nextCursor) = nextInstruction(cursor)
            instructions.add(instruction)
            cursor = nextCursor
        }
        return instructions
    }
    fun nextInstruction(cursor:CodeCursor):Pair<Instruction, CodeCursor>{
        val opCodeByte = cursor.opCodeByte()
        val opCode = OpCode.fromUByte(opCodeByte)
        val operandType = opCode.operandType
        val factory = factoryMap[operandType]
            ?: throw IllegalArgumentException("No factory for operand type $operandType with opCode $opCode")
        return factory(opCode, cursor)
    }
    private val factoryMap = mapOf<OperandType, (OpCode, CodeCursor) -> Pair<Instruction, CodeCursor>>(
        InstructionNoArg.OPERAND_TYPE to InstructionNoArg::fromCodeCursor
    )
}