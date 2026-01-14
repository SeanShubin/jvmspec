package com.seanshubin.jvmspec.domain.classfile.structure

import com.seanshubin.jvmspec.domain.classfile.io.DataInputExtensions.readByteList
import java.io.ByteArrayInputStream
import java.io.DataInput
import java.io.DataInputStream

data class AttributeCodeInfo(
    override val attributeIndex: UShort,
    override val attributeLength: Int,
    override val info: List<Byte>,
    val maxStack: UShort,
    val maxLocals: UShort,
    val codeLength: Int,
    val code: List<Byte>,
    val instructions: List<InstructionAndBytes>,
    val exceptionTableLength: UShort,
    val exceptionTable: List<ExceptionTable>,
    val attributesCount: UShort,
    val attributes: List<AttributeInfo>
) : AttributeInfo {
    companion object {
        const val NAME = "Code"
        fun fromAttributeInfo(
            attributeInfo: AttributeInfo,
            constantPoolMap: Map<UShort, ConstantInfo>,
            attributeInfoFromDataInput: (DataInput, Map<UShort, ConstantInfo>) -> AttributeInfo
        ): AttributeCodeInfo {
            val input = DataInputStream(ByteArrayInputStream(attributeInfo.info.toByteArray()))
            val maxStack = input.readUnsignedShort().toUShort()
            val maxLocals = input.readUnsignedShort().toUShort()
            val codeLength = input.readInt()
            val code = input.readByteList(codeLength)
            val instructions = InstructionFactory.allInstructions(code)
            val exceptionTableLength = input.readUnsignedShort().toUShort()
            val exceptionTable = (0 until exceptionTableLength.toInt()).map {
                ExceptionTable.fromDataInput(input)
            }
            val attributesCount = input.readUnsignedShort().toUShort()
            val attributes = (0 until attributesCount.toInt()).map {
                attributeInfoFromDataInput(input, constantPoolMap)
            }
            return AttributeCodeInfo(
                attributeInfo.attributeIndex,
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