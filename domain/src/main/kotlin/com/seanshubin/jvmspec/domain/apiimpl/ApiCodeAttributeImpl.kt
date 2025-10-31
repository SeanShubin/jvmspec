package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.*
import com.seanshubin.jvmspec.domain.data.AttributeCodeInfo

class ApiCodeAttributeImpl(
    private val apiClass: ApiClass,
    private val attributeCodeInfo: AttributeCodeInfo
) : ApiCodeAttribute {
    override fun name(): String {
        val nameIndex = attributeCodeInfo.attributeIndex
        val name = apiClass.lookupUtf8(nameIndex)
        return name
    }

    override fun bytes(): List<Byte> {
        return attributeCodeInfo.info
    }

    override fun complexity(): Int {
        return attributeCodeInfo.instructions.sumOf { it.instruction.cyclomaticComplexity() }
    }

    override fun opcodes(): List<String> {
        return attributeCodeInfo.instructions.map { it.instruction.opcode.name.lowercase() }
    }

    override fun instructions(): List<ApiInstruction> {
        return attributeCodeInfo.instructions.map {
            ApiInstructionImpl(apiClass, it)
        }
    }

    override fun exceptionTable(): List<ApiExceptionTable> {
        return attributeCodeInfo.exceptionTable.map {
            ApiExceptionTable(
                it.startProgramCounter,
                it.endProgramCounter,
                it.handlerProgramCounter,
                it.catchType
            )
        }
    }

    override fun attributes(): List<ApiAttribute> {
        return attributeCodeInfo.attributes.map {
            ApiAttributeImpl(apiClass, it)
        }
    }
}
