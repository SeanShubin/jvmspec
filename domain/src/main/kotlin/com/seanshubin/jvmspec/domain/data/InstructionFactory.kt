package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.io.IndexedDataInput
import com.seanshubin.jvmspec.domain.util.DataFormat
import com.seanshubin.jvmspec.domain.util.DataFormat.asHex
import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

object InstructionFactory {
    fun allInstructions(code:List<Byte>):List<Instruction>{
        println(code.asHex())
        val input = IndexedDataInput(DataInputStream(ByteArrayInputStream(code.toByteArray())))
        val instructions = mutableListOf<Instruction>()
        while (input.index < code.size) {
            val instruction = nextInstruction(input)
            instructions.add(instruction)
        }
        return instructions
    }
    fun nextInstruction(input:IndexedDataInput):Instruction{
        val index = input.index
        val opCodeByte = input.readUnsignedByte().toUByte()
        val opCode = OpCode.fromUByte(opCodeByte)
        val operandType = opCode.operandType
        val factory = factoryMap[operandType]
            ?: throw IllegalArgumentException("No factory for operand type $operandType with opCode $opCode")
        val instruction = factory(opCode, input, index)
        println("[$index] ${instruction.line()}")
        return instruction
    }
    private val factoryMap = mapOf<OperandType, (OpCode, DataInput, Int) -> Instruction>(
        InstructionNoArg.OPERAND_TYPE to InstructionNoArg::fromDataInput,
        InstructionLocalVariableIndex.OPERAND_TYPE to InstructionLocalVariableIndex::fromDataInput,
        InstructionConstantPoolIndex.OPERAND_TYPE to InstructionConstantPoolIndex::fromDataInput,
        InstructionConstantPoolIndexThenCountThenZero.OPERAND_TYPE to InstructionConstantPoolIndexThenCountThenZero::fromDataInput,
        InstructionBranchOffset.OPERAND_TYPE to InstructionBranchOffset::fromDataInput,
        InstructionConstantPoolByteSizedIndex.OPERAND_TYPE to InstructionConstantPoolByteSizedIndex::fromDataInput,
        InstructionConstantPoolIndexThenTwoZeroes.OPERAND_TYPE to InstructionConstantPoolIndexThenTwoZeroes::fromDataInput,
        InstructionIndexConst.OPERAND_TYPE to InstructionIndexConst::fromDataInput,
        InstructionByte.OPERAND_TYPE to InstructionByte::fromDataInput,
        InstructionArrayType.OPERAND_TYPE to InstructionArrayType::fromDataInput,
        InstructionShort.OPERAND_TYPE to InstructionShort::fromDataInput
    )
}
/*

 */