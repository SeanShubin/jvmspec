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
        fun fromDataInput(opCode: OpCode, dataInput: DataInput, index: Int): Instruction {
            val paddingSize = (-index - 1).mod(4)
            val padding = List(paddingSize) { dataInput.readByte() }
            val default = dataInput.readInt()
            val low = dataInput.readInt()
            val high = dataInput.readInt()
            val jumpOffsetCount = high - low + 1
            val jumpOffsets = List(jumpOffsetCount) {
                dataInput.readInt()
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
