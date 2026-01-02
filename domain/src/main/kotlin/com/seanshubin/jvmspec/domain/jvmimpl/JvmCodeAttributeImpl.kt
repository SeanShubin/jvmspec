package com.seanshubin.jvmspec.domain.jvmimpl

import com.seanshubin.jvmspec.domain.data.AttributeCodeInfo
import com.seanshubin.jvmspec.domain.jvm.*

class JvmCodeAttributeImpl(
    private val jvmClass: JvmClass,
    private val attributeCodeInfo: AttributeCodeInfo,
) : JvmCodeAttribute {
    override val maxStack: UShort = attributeCodeInfo.maxStack
    override val maxLocals: UShort = attributeCodeInfo.maxLocals
    override val codeLength: Int = attributeCodeInfo.codeLength
    override fun name(): String {
        val nameIndex = attributeCodeInfo.attributeIndex
        val name = jvmClass.lookupUtf8(nameIndex)
        return name
    }

    override fun bytes(): List<Byte> {
        return attributeCodeInfo.info
    }

    override fun complexity(): Int {
        return attributeCodeInfo.instructions.sumOf { it.instruction.complexity() }
    }

    override fun instructions(): List<JvmInstruction> {
        return attributeCodeInfo.instructions.map {
            JvmInstructionImpl(jvmClass, it)
        }
    }

    override fun exceptionTable(): List<JvmExceptionTable> {
        return attributeCodeInfo.exceptionTable.map {
            JvmExceptionTable(
                it.startProgramCounter,
                it.endProgramCounter,
                it.handlerProgramCounter,
                it.catchType
            )
        }
    }

    override fun attributes(): List<JvmAttribute> {
        return attributeCodeInfo.attributes.map {
            JvmAttributeImpl(jvmClass, it)
        }
    }
}
