package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

class InstructionWideFormat2(
    override val opcode:OpCode,
    val modifiedOpCode:OpCode,
    val localVariableIndex: UShort,
    val constant:Short
) :InstructionWide(opcode) {
    override fun line(): String {
        return "${opcode.formatted} ${modifiedOpCode.formatted} $localVariableIndex $constant"
    }
    companion object {
        fun fromDataInput(opCode:OpCode, modifiedOpCode:OpCode, dataInput: DataInput, index:Int): Instruction{
            val localVariableIndex = dataInput.readUnsignedShort().toUShort()
            val constant = dataInput.readShort()
            return InstructionWideFormat2(
                opcode = opCode,
                modifiedOpCode = modifiedOpCode,
                localVariableIndex = localVariableIndex,
                constant = constant
            )
        }
    }
}
