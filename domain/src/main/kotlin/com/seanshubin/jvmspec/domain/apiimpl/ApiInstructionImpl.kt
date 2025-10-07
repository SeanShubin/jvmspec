package com.seanshubin.jvmspec.domain.apiimpl

import com.seanshubin.jvmspec.domain.api.ApiInstruction
import com.seanshubin.jvmspec.domain.api.ApiRef
import com.seanshubin.jvmspec.domain.data.AttributeCodeInfo
import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.data.InstructionConstantPoolIndex

data class ApiInstructionImpl(
    val classFile: ClassFile,
    val attributeCodeInfo: AttributeCodeInfo,
    val instructionIndex: Int
) : ApiInstruction {
    private val instructionAndBytes = attributeCodeInfo.instructions[instructionIndex]
    private val instruction = instructionAndBytes.instruction
    override fun opcode(): String {
        return instruction.opcode.name.lowercase()
    }

    override fun line(): String {
        return instruction.line(classFile.constantPoolLookup)
    }

    override fun arg1Ref(): ApiRef {
        instruction as InstructionConstantPoolIndex
        val index = instruction.constantPoolIndex
        val constantPoolLookup = classFile.constantPoolLookup
        val (className, methodName, methodDescriptor) = constantPoolLookup.ref(index)
        val signature = DescriptorParser.build(methodDescriptor)
        return ApiRef(className, methodName, signature)
    }

    override fun arg1ClassName(): String {
        instruction as InstructionConstantPoolIndex
        val index = instruction.constantPoolIndex
        val constantPoolLookup = classFile.constantPoolLookup
        val className = constantPoolLookup.className(index)
        return className
    }
}
