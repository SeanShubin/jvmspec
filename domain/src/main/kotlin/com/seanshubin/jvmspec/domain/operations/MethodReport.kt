package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.aggregation.Aggregator
import com.seanshubin.jvmspec.domain.aggregation.QualifiedMethod
import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.command.WriteLines
import com.seanshubin.jvmspec.domain.data.*
import java.nio.file.Path

class MethodReport(private val aggregator: Aggregator) : Report {
    override fun reportCommands(baseFileName: String, outputDir: Path, classFile: ClassFile): List<Command> {
        aggregator.classFile(classFile)
        val className = classFile.thisClassName()
        val methods = classFile.methods
        val constantPoolLookup = classFile.constantPoolLookup
        val lines = mutableListOf<String>()
        val targetOpCodes = listOf(
            OpCode.INVOKESTATIC,
            OpCode.GETSTATIC,
            OpCode.PUTSTATIC,
            OpCode.NEW
        )
        methods.forEach { method ->
            val methodName = constantPoolLookup.lookupUtf8Value(method.nameIndex)
            val methodDescriptor = constantPoolLookup.lookupUtf8Value(method.descriptorIndex)
            val source = QualifiedMethod(className, methodName, methodDescriptor)
            lines.add("$methodName.$methodDescriptor")
            method.attributes.filterIsInstance<AttributeCodeInfo>().forEach { codeAttribute ->
                val cyclomaticComplexity = codeAttribute.instructions.sumOf { it.instruction.cyclomaticComplexity() }
                aggregator.cyclomaticComplexity(source, cyclomaticComplexity)
                codeAttribute.instructions.forEach { instructionAndBytes ->
                    val instruction = instructionAndBytes.instruction
                    val opCode = instruction.opcode
                    if (targetOpCodes.contains(opCode)) {
                        val line = instruction.line(constantPoolLookup)
                        lines.add("  $line")
                    }
                    if (opCode == OpCode.INVOKESTATIC) {
                        val target = getQualifiedMethod(instruction, constantPoolLookup)
                        aggregator.invokeStatic(source, target)
                    }
                    if (opCode == OpCode.GETSTATIC) {
                        val target = getQualifiedMethod(instruction, constantPoolLookup)
                        aggregator.getStatic(source, target)
                    }
                    if (opCode == OpCode.PUTSTATIC) {
                        val target = getQualifiedMethod(instruction, constantPoolLookup)
                        aggregator.putStatic(source, target)
                    }
                    if (opCode == OpCode.NEW) {
                        val target = getTargetClass(instruction, constantPoolLookup)
                        aggregator.newInstance(source, target)
                    }
                }
            }
        }
        val outputFile = outputDir.resolve("$baseFileName-methods.txt")
        val commands = listOf(
            WriteLines(outputFile, lines)
        )
        return commands
    }

    private fun getQualifiedMethod(instruction: Instruction, constantPoolLookup: ConstantPoolLookup): QualifiedMethod {
        instruction as InstructionConstantPoolIndex
        val index = instruction.constantPoolIndex
        val (className, methodName, methodDescriptor) = constantPoolLookup.ref(index)
        return QualifiedMethod(className, methodName, methodDescriptor)
    }

    private fun getTargetClass(instruction: Instruction, constantPoolLookup: ConstantPoolLookup): String {
        instruction as InstructionConstantPoolIndex
        val index = instruction.constantPoolIndex
        val className = constantPoolLookup.className(index)
        return className
    }
}
