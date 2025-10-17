package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiCodeAttribute
import com.seanshubin.jvmspec.domain.api.ApiInstruction
import com.seanshubin.jvmspec.domain.data.AttributeCodeInfo
import com.seanshubin.jvmspec.domain.data.ClassFile

class ApiCodeAttributeImpl(
    private val classFile: ClassFile,
    private val methodIndex: Int,
    private val attributeIndex: Int
) : ApiCodeAttribute {
    private val methodInfo = classFile.methods[methodIndex]
    private val attributeInfo = methodInfo.attributes[attributeIndex] as AttributeCodeInfo
    override fun name(): String {
        val nameIndex = attributeInfo.attributeIndex
        val name = classFile.constantPoolLookup.lookupUtf8Value(nameIndex)
        return name
    }

    override fun bytes(): List<Byte> {
        return attributeInfo.info
    }
    override fun complexity(): Int {
        return attributeInfo.instructions.sumOf { it.instruction.cyclomaticComplexity() }
    }

    override fun opcodes(): List<String> {
        return attributeInfo.instructions.map { it.instruction.opcode.name.lowercase() }
    }

    override fun instructions(): List<ApiInstruction> {
        return attributeInfo.instructions.indices.map { instructionIndex ->
            ApiInstructionImpl(classFile, attributeInfo, instructionIndex)
        }
    }
}
