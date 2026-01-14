package com.seanshubin.jvmspec.domain.classfile.structure

import java.io.DataInput

object InstructionWide {
    val OPERAND_TYPE = OperandType.WIDE
    val format1OpCodes = setOf(
        OpCode.ALOAD,
        OpCode.ILOAD,
        OpCode.FLOAD,
        OpCode.DLOAD,
        OpCode.LLOAD,
        OpCode.ASTORE,
        OpCode.ISTORE,
        OpCode.FSTORE,
        OpCode.DSTORE,
        OpCode.LSTORE,
        OpCode.RET
    )
    val format2OpCodes = setOf(
        OpCode.IINC
    )

    fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
        val modifiedOpCodeByte = input.readUnsignedByte().toUByte()
        val modifiedOpCode = OpCode.fromUByte(modifiedOpCodeByte)
        val instruction = if (format1OpCodes.contains(modifiedOpCode)) {
            InstructionWideFormat1.fromDataInput(opCode, modifiedOpCode, input, index)
        } else if (format2OpCodes.contains(modifiedOpCode)) {
            InstructionWideFormat2.fromDataInput(opCode, modifiedOpCode, input, index)
        } else {
            throw IllegalArgumentException("Unexpected opcode $opCode for wide instruction at index $index")
        }
        return instruction
    }
}
