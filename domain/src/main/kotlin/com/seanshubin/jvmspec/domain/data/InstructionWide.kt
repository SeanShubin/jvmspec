package com.seanshubin.jvmspec.domain.data

import java.io.DataInput

abstract class InstructionWide(
    override val opcode: OpCode,
) : Instruction {
    companion object {
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

        fun fromDataInput(opCode: OpCode, dataInput: DataInput, index: Int): Instruction {
            val modifiedOpCodeByte = dataInput.readUnsignedByte().toUByte()
            val modifiedOpCode = OpCode.fromUByte(modifiedOpCodeByte)
            val instruction = if (format1OpCodes.contains(modifiedOpCode)) {
                InstructionWideFormat1.fromDataInput(opCode, modifiedOpCode, dataInput, index)
            } else if (format2OpCodes.contains(modifiedOpCode)) {
                InstructionWideFormat2.fromDataInput(opCode, modifiedOpCode, dataInput, index)
            } else {
                throw IllegalArgumentException("Unexpected opcode $opCode for wide instruction at index $index")
            }
            return instruction
        }
    }
}
