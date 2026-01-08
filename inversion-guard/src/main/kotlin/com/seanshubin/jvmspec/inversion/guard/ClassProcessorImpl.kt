package com.seanshubin.jvmspec.inversion.guard

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
    override fun processClass(jvmClass: JvmClass, classAnalysis: ClassAnalysis): List<Command> {
        val relativePath = baseDir.relativize(classAnalysis.path)
        val baseFileName = outputDir.resolve(relativePath).removeExtension("class")
        val analysisCommands = createAnalysisCommands(baseFileName, classAnalysis)
        val disassemblyCommands = createDisassemblyCommands(baseFileName, jvmClass)
        val allCommands = analysisCommands + disassemblyCommands
        return allCommands
    }

    private fun createAnalysisCommands(baseFileName: String, classAnalysis: ClassAnalysis): List<Command> {
        val outputFile = Path.of("$baseFileName-analysis.txt")
        val methodNodes = classAnalysis.methodAnalysisList.mapIndexed { index, methodAnalysis ->
            val javaMethod = methodAnalysis.signature.javaFormat(methodAnalysis.className, methodAnalysis.methodName)
            val compactMethod =
                methodAnalysis.signature.compactFormat(methodAnalysis.className, methodAnalysis.methodName)
            val staticInvocationChildren = methodAnalysis.staticInvocations.mapIndexed { index, invocationAnalysis ->
                val invocationJava =
                    invocationAnalysis.signature.javaFormat(invocationAnalysis.className, invocationAnalysis.methodName)
                val invocationCompact = invocationAnalysis.signature.compactFormat(
                    invocationAnalysis.className,
                    invocationAnalysis.methodName
                )
                val invocationNodes = listOf(
                    Tree("java: $invocationJava"),
                    Tree("compact: $invocationCompact"),
                    Tree("invocationOpcodeName: ${invocationAnalysis.invocationOpcodeName}"),
                    Tree("invocationType: ${invocationAnalysis.invocationType}")
                )
                Tree("Invocation[$index]", invocationNodes)
            }
            val methodChildren = listOf(
                Tree("java: $javaMethod"),
                Tree("compact: $compactMethod"),
                Tree("complexity: ${methodAnalysis.complexity}"),
                Tree("categories: ${methodAnalysis.boundaryLogicCategories.joinToString(",")}"),
                Tree(
                    "staticInvocations(${methodAnalysis.staticInvocations.size}): ${methodAnalysis.methodName}",
                    staticInvocationChildren
                ),
                Tree("isBoundaryLogic: ${methodAnalysis.isBoundaryLogic()}")
            )
            Tree("Method[$index]", methodChildren)
        }
        val summaryRoots = listOf(
            Tree("Class: ${classAnalysis.name}"),
            Tree("Origin: ${classAnalysis.path}"),
            Tree("Complexity: ${classAnalysis.complexity()}"),
            Tree("Methods(${classAnalysis.methodAnalysisList.size})", methodNodes)
        )
        val command = CreateFileCommand(outputFile, summaryRoots)
        return listOf(command)
    }

    private fun createDisassemblyCommands(baseFileName: String, jvmClass: JvmClass): List<Command> {
        val treeList = format.classTreeList(jvmClass)
        val filePath = Path.of("$baseFileName-disassembled.txt")
        val disassemblyCommand = CreateFileCommand(filePath, treeList)
        return listOf(disassemblyCommand)
    }
}