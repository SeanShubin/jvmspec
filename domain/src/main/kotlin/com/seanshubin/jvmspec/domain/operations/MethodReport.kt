package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.command.WriteLines
import com.seanshubin.jvmspec.domain.data.AttributeCodeInfo
import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.data.OpCode
import java.nio.file.Path

class MethodReport : Report {
    override fun reportCommands(baseFileName: String, outputDir: Path, classFile: ClassFile): List<Command> {
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
            lines.add("$methodName.$methodDescriptor")
            method.attributes.filterIsInstance<AttributeCodeInfo>().forEach { codeAttribute ->
                codeAttribute.instructions.forEach { instructionAndBytes ->
                    val opCode = instructionAndBytes.instruction.opcode
                    if (targetOpCodes.contains(opCode)) {
                        val line = instructionAndBytes.instruction.line(constantPoolLookup)
                        lines.add("  $line")
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
}
