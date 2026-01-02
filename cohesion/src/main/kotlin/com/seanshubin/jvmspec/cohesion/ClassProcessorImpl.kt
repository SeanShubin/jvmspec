package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.format.JvmSpecFormat
import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.tree.Tree
import com.seanshubin.jvmspec.domain.util.PathUtil.removeExtension
import java.nio.file.Path

class ClassProcessorImpl(
    private val baseDir: Path,
    private val outputDir: Path,
    private val format: JvmSpecFormat
) : ClassProcessor {
    override fun processClass(jvmClass: JvmClass): List<Command> {
        val relativePath = baseDir.relativize(jvmClass.origin)
        val baseFileName = outputDir.resolve(relativePath).removeExtension("class")
        val analysisCommands = createAnalysisCommands(baseFileName, jvmClass)
        val disassemblyCommands = createDisassemblyCommands(baseFileName, jvmClass)
        val allCommands = analysisCommands + disassemblyCommands
        return allCommands
    }

    private fun createAnalysisCommands(baseFileName: String, jvmClass: JvmClass): List<Command> {
        val outputFile = Path.of("$baseFileName-analysis.txt")
        val complexity = jvmClass.complexity()
        val summaryRoots = listOf(
            Tree("Class: ${jvmClass.thisClassName}"),
            Tree("Origin: ${jvmClass.origin}"),
            Tree("Complexity: $complexity")
        )
        val methodChildren: List<Tree> = jvmClass.methods().mapIndexed { index, method ->
            val instructions = method.instructions()
            val instructionRoots = instructions.map { Tree(it.name()) }
            val methodHeader = "[$index]: complexity(${method.complexity()}) ${method.javaSignature()}"
            val methodRoot = Tree(methodHeader, instructionRoots)
            methodRoot
        }
        val methodsHeader = "Methods(${jvmClass.methods().size}):"
        val methodsRoot: Tree = Tree(methodsHeader, methodChildren)
        val methodRoots = listOf(methodsRoot)
        val roots: List<Tree> = summaryRoots + methodRoots
        val command = CreateFileCommand(outputFile, roots)
        return listOf(command)
    }

    private fun createDisassemblyCommands(baseFileName: String, jvmClass: JvmClass): List<Command> {
        val treeList = format.classTreeList(jvmClass)
        val filePath = Path.of("$baseFileName-disassembled.txt")
        val disassemblyCommand = CreateFileCommand(filePath, treeList)
        return listOf(disassemblyCommand)
    }
}