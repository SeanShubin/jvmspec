package com.seanshubin.jvmspec.domain.jvmimpl

import com.seanshubin.jvmspec.domain.data.*
import com.seanshubin.jvmspec.domain.jvm.JvmArgument
import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.jvm.JvmInstruction

data class JvmInstructionImpl(
    val jvmClass: JvmClass,
    val instructionAndBytes: InstructionAndBytes
) : JvmInstruction {
    private val instruction = instructionAndBytes.instruction
    private val opcode = instruction.opcode
    private val operandType = opcode.operandType

    override fun name(): String {
        return opcode.name.lowercase()
    }

    override fun code(): UByte {
        return opcode.ubyte
    }

    override fun args(): List<JvmArgument> {
        return when (operandType) {
            OperandType.NONE -> emptyList()
            OperandType.LOCAL_VARIABLE_INDEX -> localVariableIndexArgs()
            OperandType.CONSTANT_POOL_INDEX -> constantPoolIndexArgs()
            OperandType.BYTE -> byteArgs()
            OperandType.BRANCH_OFFSET -> branchOffsetArgs()
            OperandType.BRANCH_OFFSET_WIDE -> branchOffsetWideArgs()
            OperandType.INDEX_CONST -> indexConstArgs()
            OperandType.CONSTANT_POOL_INDEX_THEN_TWO_ZEROES -> constantPoolIndexThenTwoZerosArgs()
            OperandType.CONSTANT_POOL_INDEX_THEN_COUNT_THEN_ZERO -> constantPoolIndexThenCountThenZeroArgs()
            OperandType.CONSTANT_POOL_BYTE_SIZED_INDEX -> constantPoolByteSizedIndexArgs()
            OperandType.LOOKUP_SWITCH -> lookupSwitchArgs()
            OperandType.CONSTANT_POOL_INDEX_THEN_DIMENSIONS -> constantPoolIndexThenDimensionsArgs()
            OperandType.ARRAY_TYPE -> arrayTypeArgs()
            OperandType.SHORT -> shortArgs()
            OperandType.TABLE_SWITCH -> tableSwitchArgs()
            OperandType.WIDE -> wideArgs()
        }
    }

    private fun constantPoolIndexArgs(): List<JvmArgument> {
        instruction as InstructionConstantPoolIndex
        return listOf(constantPoolIndexToArg(instruction.constantPoolIndex))
    }

    private fun constantPoolByteSizedIndexArgs(): List<JvmArgument> {
        instruction as InstructionConstantPoolByteSizedIndex
        return listOf(constantPoolIndexToArg(instruction.constantPoolIndex.toUShort()))
    }

    private fun localVariableIndexArgs(): List<JvmArgument> {
        instruction as InstructionLocalVariableIndex
        return listOf(JvmArgument.IntValue(instruction.localVariableIndex))
    }

    private fun constantPoolIndexThenTwoZerosArgs(): List<JvmArgument> {
        instruction as InstructionConstantPoolIndexThenTwoZeroes
        return listOf(constantPoolIndexToArg(instruction.constantPoolIndex))
    }

    private fun byteArgs(): List<JvmArgument> {
        instruction as InstructionByte
        return listOf(JvmArgument.IntValue(instruction.value.toInt()))
    }

    private fun branchOffsetArgs(): List<JvmArgument> {
        instruction as InstructionBranchOffset
        return listOf(JvmArgument.IntValue(instruction.branchOffset.toInt()))
    }

    private fun branchOffsetWideArgs(): List<JvmArgument> {
        instruction as InstructionBranchOffsetWide
        return listOf(JvmArgument.IntValue(instruction.offset))
    }

    private fun indexConstArgs(): List<JvmArgument> {
        instruction as InstructionIndexConst
        return listOf(
            JvmArgument.IntValue(instruction.index),
            JvmArgument.IntValue(instruction.const.toInt())
        )
    }

    private fun constantPoolIndexThenCountThenZeroArgs(): List<JvmArgument> {
        instruction as InstructionConstantPoolIndexThenCountThenZero
        val constant = constantPoolIndexToArg(instruction.constantPoolIndex)
        return listOf(
            constant,
            JvmArgument.IntValue(instruction.count)
        )
    }

    private fun lookupSwitchArgs(): List<JvmArgument> {
        instruction as InstructionLookupSwitch
        val pairs = instruction.pairs.map { (first, second) ->
            first to second
        }
        return listOf(JvmArgument.LookupSwitch(instruction.default, pairs))
    }

    private fun constantPoolIndexThenDimensionsArgs(): List<JvmArgument> {
        instruction as InstructionConstantPoolIndexThenDimensions
        return listOf(
            constantPoolIndexToArg(instruction.classIndex),
            JvmArgument.IntValue(instruction.dimensions)
        )
    }

    private fun arrayTypeArgs(): List<JvmArgument> {
        instruction as InstructionArrayType
        return listOf(JvmArgument.ArrayTypeValue(instruction.arrayType))
    }

    private fun shortArgs(): List<JvmArgument> {
        instruction as InstructionShort
        return listOf(JvmArgument.IntValue(instruction.value.toInt()))
    }

    private fun tableSwitchArgs(): List<JvmArgument> {
        instruction as InstructionTableSwitch
        return listOf(
            JvmArgument.TableSwitch(
                instruction.default,
                instruction.low,
                instruction.high,
                instruction.jumpOffsets
            )
        )
    }

    private fun wideArgs(): List<JvmArgument> {
        return when (instruction) {
            is InstructionWideFormat1 -> wideArgs1()
            is InstructionWideFormat2 -> wideArgs2()
            else -> throw UnsupportedOperationException("unknown wide instruction type: ${instruction::class}")
        }
    }

    private fun wideArgs1(): List<JvmArgument> {
        instruction as InstructionWideFormat1
        return listOf(
            JvmArgument.OpCodeValue(instruction.opcode.name, instruction.opcode.ubyte),
            JvmArgument.IntValue(instruction.localVariableIndex.toInt())
        )
    }

    private fun wideArgs2(): List<JvmArgument> {
        instruction as InstructionWideFormat2
        return listOf(
            JvmArgument.OpCodeValue(instruction.opcode.name, instruction.opcode.ubyte),
            JvmArgument.IntValue(instruction.localVariableIndex.toInt()),
            JvmArgument.IntValue(instruction.constant.toInt())
        )
    }

    private fun constantPoolIndexToArg(constantPoolIndex: UShort): JvmArgument {
        val constant = jvmClass.constants.getValue(constantPoolIndex)
        return JvmArgument.Constant(constant)
    }
}
