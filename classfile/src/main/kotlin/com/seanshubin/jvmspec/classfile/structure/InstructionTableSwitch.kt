package com.seanshubin.jvmspec.classfile.structure

import java.io.DataInput

class InstructionTableSwitch(
    override val opcode: OpCode,
    val padding: List<Byte>,
    val default: Int,
    val low: Int,
    val high: Int,
    val jumpOffsets: List<Int>,
) : Instruction {
    override fun complexity(): Int = jumpOffsets.size

    companion object {
        val OPERAND_TYPE = OperandType.TABLE_SWITCH
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val paddingSize = (-index - 1).mod(4)
            val padding = List(paddingSize) { input.readByte() }
            val default = input.readInt()
            val low = input.readInt()
            val high = input.readInt()
            val jumpOffsetCount = high - low + 1
            val jumpOffsets = List(jumpOffsetCount) {
                input.readInt()
            }
            return InstructionTableSwitch(
                opcode = opCode,
                padding = padding,
                default = default,
                low = low,
                high = high,
                jumpOffsets = jumpOffsets
            )
        }
    }
}
