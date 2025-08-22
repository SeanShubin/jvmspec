package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataFormat.toDecHex
import com.seanshubin.jvmspec.domain.util.DataFormat.toHex
import java.io.DataInput

class InstructionLookupSwitch(
    override val opcode: OpCode,
    val padding: List<Byte>,
    val default: Int,
    val nPairs: Int,
    val pairs: List<MatchOffset>,
) : Instruction {
    override fun line(constantPoolLookup: ConstantPoolLookup): String {
        val pairsString = pairs.joinToString(" ") { (match, offset) -> "${match.toDecHex()}:${offset.toDecHex()}" }
        return "${opcode.line} padding(0x${padding.toHex()}) ${default.toDecHex()} ${nPairs.toDecHex()} $pairsString"
    }

    companion object {
        val OPERAND_TYPE = OperandType.LOOKUP_SWITCH
        fun fromDataInput(opCode: OpCode, input: DataInput, index: Int): Instruction {
            val paddingSize = (-index - 1).mod(4)
            val padding = List(paddingSize) { input.readByte() }
            val default = input.readInt()
            val nPairs = input.readInt()
            val pairs = List(nPairs) {
                val match = input.readInt()
                val offset = input.readInt()
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
