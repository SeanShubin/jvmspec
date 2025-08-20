package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.asHex
import java.io.DataInput

class InstructionLookupSwitch(
    override val opcode: OpCode,
    val padding: List<Byte>,
    val default: Int,
    val nPairs: Int,
    val pairs: List<MatchOffset>,
) : Instruction {
    override fun line(): String {
        val pairsString = pairs.joinToString(" ") { (match, offset) -> "$match:$offset" }
        return "${opcode.formatted} padding(0x${padding.asHex()}) $default $nPairs $pairsString"
    }

    companion object {
        val OPERAND_TYPE = OperandType.LOOKUP_SWITCH
        fun fromDataInput(opCode: OpCode, dataInput: DataInput, index: Int): Instruction {
            val paddingSize = (-index - 1).mod(4)
            val padding = List(paddingSize) { dataInput.readByte() }
            val default = dataInput.readInt()
            val nPairs = dataInput.readInt()
            val pairs = List(nPairs) {
                val match = dataInput.readInt()
                val offset = dataInput.readInt()
                MatchOffset(match, offset)
            }
            return InstructionLookupSwitch(
                opcode = opCode,
                padding = padding,
                default = default,
                nPairs = nPairs,
                pairs = pairs
            )
        }
    }
}
