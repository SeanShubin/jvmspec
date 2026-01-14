package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

class InstructionWideFormat2(
    override val opcode: OpCode,
    val modifiedOpCode: OpCode,
    val localVariableIndex: UShort,
    val constant: Short
) : Instruction {
    override fun complexity(): Int = 0

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
