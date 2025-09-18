package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import java.io.DataInput

class InstructionBranchOffset(
    override val opcode: OpCode,
    val branchOffset: Short
) : Instruction {
    override fun line(constantPoolLookup: ConstantPoolLookup): String {
        return "${opcode.line} ${branchOffset.toDecHex()}"
    }

    override fun cyclomaticComplexity(): Int = 1

    companion object {
        val OPERAND_TYPE = OperandType.BRANCH_OFFSET
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val branchOffset = input.readShort()
            val instruction = InstructionBranchOffset(opCode, branchOffset)
            return instruction
        }
    }
}
