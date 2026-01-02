package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.util.PathUtil.replaceExtension
import java.nio.file.Path

class ClassProcessorImpl(
    private val baseDir: Path,
    private val outputDir: Path
) : ClassProcessor {
    override fun processClass(jvmClass: JvmClass): List<Command> {
        val relativePath = baseDir.relativize(jvmClass.origin)
        val outputPath = outputDir.resolve(relativePath).replaceExtension("class", "txt")
        val reportContents = generateReport(jvmClass)
        val createFileCommand = CreateFileCommand(outputPath, reportContents)
        return listOf(createFileCommand)
    }

    private fun generateReport(jvmClass: JvmClass): List<String> {
        val complexity = jvmClass.complexity()
        val summaryLines = listOf(
            "Class: ${jvmClass.thisClassName}",
            "Origin: ${jvmClass.origin}",
            "Complexity: $complexity"
        )
        val methodHeader = listOf("Methods(${jvmClass.methods().size}):")
        val methodLines = jvmClass.methods().flatMapIndexed { index, method ->
            val instructions = method.instructions()
            val instructionNames = instructions.map { instruction -> "    " + instruction.name() }
            listOf("  [$index]: complexity(${method.complexity()}) ${method.javaSignature()}") + instructionNames
        }
        val lines = summaryLines + methodHeader + methodLines
        return lines
    }
}