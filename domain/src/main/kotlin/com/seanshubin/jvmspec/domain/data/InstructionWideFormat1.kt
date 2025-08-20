package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import java.io.DataInput

class InstructionWideFormat1(
    override val opcode: OpCode,
    val modifiedOpCode: OpCode,
    val localVariableIndex: UShort
) : InstructionWide(opcode) {
    override fun line(): String {
        return "${opcode.line} ${modifiedOpCode.line} ${localVariableIndex.toDecHex()}"
    }

    companion object {
        fun fromDataInput(opCode: OpCode, modifiedOpCode: OpCode, dataInput: DataInput, index: Int): Instruction {
            val localVariableIndex = dataInput.readUnsignedShort().toUShort()
            return InstructionWideFormat1(
                opcode = opCode,
                modifiedOpCode = modifiedOpCode,
                localVariableIndex = localVariableIndex
            )
        }
    }
}
