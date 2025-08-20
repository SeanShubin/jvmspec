package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import java.io.DataInput

class InstructionBranchOffset(
    override val opcode: OpCode,
    val branchOffset: Short
) : Instruction {
    override fun line(): String {
        return "${opcode.line} ${branchOffset.toDecHex()}"
    }

    companion object {
        val OPERAND_TYPE = OperandType.BRANCH_OFFSET
        fun fromDataInput(opCode: OpCode, dataInput: DataInput, index: Int): Instruction {
            val branchOffset = dataInput.readShort()
            val instruction = InstructionBranchOffset(opCode, branchOffset)
            return instruction
        }
    }
}
