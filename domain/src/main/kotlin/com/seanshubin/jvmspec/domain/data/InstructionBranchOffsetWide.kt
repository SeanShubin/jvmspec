package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionBranchOffsetWide(
    override val opcode: OpCode,
    val offset: Int,
) : Instruction {
    override fun line(constantPoolLookup: ConstantPoolLookup): String {
        return "${opcode.line} $offset"
    }

    override fun cyclomaticComplexity(): Int = 1

    companion object {
        val OPERAND_TYPE = OperandType.BRANCH_OFFSET_WIDE
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val offset = input.readInt()
            return InstructionBranchOffsetWide(
                opcode = opCode,
                offset = offset
            )
        }
    }
}
