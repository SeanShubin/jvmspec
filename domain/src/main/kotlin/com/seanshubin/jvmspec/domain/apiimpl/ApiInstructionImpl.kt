package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiArgument
import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.api.ApiInstruction
import com.seanshubin.jvmspec.domain.data.*

data class ApiInstructionImpl(
    val apiClass: ApiClass,
    val instructionAndBytes: InstructionAndBytes
) : ApiInstruction {
    private val instruction = instructionAndBytes.instruction
    private val opcode = instruction.opcode
    private val operandType = opcode.operandType

    override fun name(): String {
        return opcode.name.lowercase()
    }

    override fun code(): UByte {
        return opcode.ubyte
    }

    override fun args(): List<ApiArgument> {
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

    private fun constantPoolIndexArgs(): List<ApiArgument> {
        instruction as InstructionConstantPoolIndex
        return listOf(constantPoolIndexToArg(instruction.constantPoolIndex))
    }

    private fun constantPoolByteSizedIndexArgs(): List<ApiArgument> {
        instruction as InstructionConstantPoolByteSizedIndex
        return listOf(constantPoolIndexToArg(instruction.constantPoolIndex.toUShort()))
    }

    private fun localVariableIndexArgs(): List<ApiArgument> {
        instruction as InstructionLocalVariableIndex
        return listOf(ApiArgument.IntValue(instruction.localVariableIndex))
    }

    private fun constantPoolIndexThenTwoZerosArgs(): List<ApiArgument> {
        instruction as InstructionConstantPoolIndexThenTwoZeroes
        return listOf(constantPoolIndexToArg(instruction.constantPoolIndex))
    }

    private fun byteArgs(): List<ApiArgument> {
        instruction as InstructionByte
        return listOf(ApiArgument.IntValue(instruction.value.toInt()))
    }

    private fun branchOffsetArgs(): List<ApiArgument> {
        instruction as InstructionBranchOffset
        return listOf(ApiArgument.IntValue(instruction.branchOffset.toInt()))
    }

    private fun branchOffsetWideArgs(): List<ApiArgument> {
        instruction as InstructionBranchOffsetWide
        return listOf(ApiArgument.IntValue(instruction.offset))
    }

    private fun indexConstArgs(): List<ApiArgument> {
        instruction as InstructionIndexConst
        return listOf(
            ApiArgument.IntValue(instruction.index),
            ApiArgument.IntValue(instruction.const.toInt())
        )
    }

    private fun constantPoolIndexThenCountThenZeroArgs(): List<ApiArgument> {
        instruction as InstructionConstantPoolIndexThenCountThenZero
        val constant = constantPoolIndexToArg(instruction.constantPoolIndex)
        return listOf(
            constant,
            ApiArgument.IntValue(instruction.count)
        )
    }

    private fun lookupSwitchArgs(): List<ApiArgument> {
        instruction as InstructionLookupSwitch
        val pairs = instruction.pairs.map { (first, second) ->
            first to second
        }
        return listOf(ApiArgument.LookupSwitch(instruction.default, pairs))
    }

    private fun constantPoolIndexThenDimensionsArgs(): List<ApiArgument> {
        instruction as InstructionConstantPoolIndexThenDimensions
        return listOf(
            constantPoolIndexToArg(instruction.classIndex),
            ApiArgument.IntValue(instruction.dimensions)
        )
    }

    private fun arrayTypeArgs(): List<ApiArgument> {
        instruction as InstructionArrayType
        return listOf(ApiArgument.ArrayTypeValue(instruction.arrayType))
    }

    private fun shortArgs(): List<ApiArgument> {
        instruction as InstructionShort
        return listOf(ApiArgument.IntValue(instruction.value.toInt()))
    }

    private fun tableSwitchArgs(): List<ApiArgument> {
        instruction as InstructionTableSwitch
        return listOf(
            ApiArgument.TableSwitch(
                instruction.default,
                instruction.low,
                instruction.high,
                instruction.jumpOffsets
            )
        )
    }

    private fun wideArgs(): List<ApiArgument> {
        return when (instruction) {
            is InstructionWideFormat1 -> wideArgs1()
            is InstructionWideFormat2 -> wideArgs2()
            else -> throw UnsupportedOperationException("unknown wide instruction type: ${instruction::class}")
        }
    }

    private fun wideArgs1(): List<ApiArgument> {
        instruction as InstructionWideFormat1
        return listOf(
            ApiArgument.OpCodeValue(instruction.opcode.name, instruction.opcode.ubyte),
            ApiArgument.IntValue(instruction.localVariableIndex.toInt())
        )
    }

    private fun wideArgs2(): List<ApiArgument> {
        instruction as InstructionWideFormat2
        return listOf(
            ApiArgument.OpCodeValue(instruction.opcode.name, instruction.opcode.ubyte),
            ApiArgument.IntValue(instruction.localVariableIndex.toInt()),
            ApiArgument.IntValue(instruction.constant.toInt())
        )
    }

    private fun constantPoolIndexToArg(constantPoolIndex: UShort): ApiArgument {
        val constant = apiClass.constants.getValue(constantPoolIndex)
        return ApiArgument.Constant(constant)
    }
}
