package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import com.seanshubin.jvmspec.domain.util.DataFormat.toHex
import java.io.DataInput

class InstructionTableSwitch(
    override val opcode: OpCode,
    val padding: List<Byte>,
    val default: Int,
    val low: Int,
    val high: Int,
    val jumpOffsets: List<Int>,
) : Instruction {
    override fun line(): String {
        val jumpOffsetsString = jumpOffsets.joinToString(" ") { it.toDecHex() }
        return "${opcode.line} padding(0x${padding.toHex()}) ${default.toDecHex()} ${low.toDecHex()} ${high.toDecHex()} $jumpOffsetsString"
    }

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
