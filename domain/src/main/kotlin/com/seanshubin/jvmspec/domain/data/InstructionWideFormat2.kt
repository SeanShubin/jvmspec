package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import java.io.DataInput

class InstructionWideFormat2(
    override val opcode: OpCode,
    val modifiedOpCode: OpCode,
    val localVariableIndex: UShort,
    val constant: Short
) : InstructionWide(opcode) {
    override fun line(): String {
        return "${opcode.line} ${modifiedOpCode.line} ${localVariableIndex.toDecHex()} ${constant.toDecHex()}"
    }

    companion object {
        fun fromDataInput(opCode: OpCode, modifiedOpCode: OpCode, input: DataInput, index: Int): Instruction {
            val localVariableIndex = input.readUnsignedShort().toUShort()
            val constant = input.readShort()
            return InstructionWideFormat2(
                opcode = opCode,
                modifiedOpCode = modifiedOpCode,
                localVariableIndex = localVariableIndex,
                constant = constant
            )
        }
    }
}
