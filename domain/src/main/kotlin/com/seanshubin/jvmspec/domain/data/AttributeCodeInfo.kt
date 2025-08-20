package com.seanshubin.jvmspec.domain.data

import com.seanshubin.jvmspec.domain.util.DataInputExtensions.readByteList
import java.io.ByteArrayInputStream
import java.io.DataInputStream

data class AttributeCodeInfo(
    override val attributeNameIndex: Short,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val maxStack: Short,
    val maxLocals: Short,
    val codeLength: Int,
    val code: List<Byte>,
    val instructions: List<Instruction>,
    val exceptionTableLength: Short,
    val exceptionTable: List<ExceptionTable>,
    val attributesCount: Short,
    val attributes: List<AttributeInfo>
) : AttributeInfo {
    companion object {
        const val NAME = "Code"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolLookup: ConstantPoolLookup
        ): AttributeCodeInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val maxStack = input.readShort()
            val maxLocals = input.readShort()
            val codeLength = input.readInt()
            val code = input.readByteList(codeLength)
            val instructions = InstructionFactory.allInstructions(code)
            val exceptionTableLength = input.readShort()
            val exceptionTable = (0 until exceptionTableLength).map {
                ExceptionTable.fromDataInput(input)
            }
            val attributesCount = input.readShort()
            val attributes = (0 until attributesCount).map {
                AttributeInfoFactory.fromDataInput(input, constantPoolLookup)
            }
            return AttributeCodeInfo(
                attributeInfo.attributeNameIndex,
                attributeInfo.attributeLength,
                attributeInfo.info,
                maxStack,
                maxLocals,
                codeLength,
                code,
                instructions,
                exceptionTableLength,
                exceptionTable,
                attributesCount,
                attributes
            )
        }
    }
}