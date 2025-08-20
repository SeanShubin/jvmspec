package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionWideFormat1(
    override val opcode: OpCode,
    val modifiedOpCode: OpCode,
    val localVariableIndex: UShort
) : InstructionWide(opcode) {
    override fun line(): String {
        return "${opcode.formatted} ${modifiedOpCode.formatted} $localVariableIndex"
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
