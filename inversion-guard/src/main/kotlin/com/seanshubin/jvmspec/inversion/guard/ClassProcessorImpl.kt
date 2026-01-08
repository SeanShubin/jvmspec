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
    override fun processClass(classAnalysis: ClassAnalysis): List<Command> {
        val relativePath = baseDir.relativize(classAnalysis.jvmClass.origin)
        val baseFileName = outputDir.resolve(relativePath).removeExtension("class")
        val analysisCommands = createAnalysisCommands(baseFileName, classAnalysis)
        val disassemblyCommands = createDisassemblyCommands(baseFileName, classAnalysis.jvmClass)
        val allCommands = analysisCommands + disassemblyCommands
        return allCommands
    }

    private fun createAnalysisCommands(baseFileName: String, classAnalysis: ClassAnalysis): List<Command> {
        val outputFile = Path.of("$baseFileName-analysis.txt")
        val summaryTrees = createAnalysisSummaryTrees(classAnalysis)
        val command = CreateFileCommand(outputFile, summaryTrees)
        return listOf(command)
    }

    private fun createAnalysisSummaryTrees(classAnalysis: ClassAnalysis): List<Tree> {
        val methodTrees = createMethodAnalysisTrees(classAnalysis.methodAnalysisList)
        return listOf(
            Tree("Class: ${classAnalysis.jvmClass.thisClassName}"),
            Tree("Origin: ${classAnalysis.jvmClass.origin}"),
            Tree("Complexity: ${classAnalysis.complexity()}"),
            Tree("Methods(${classAnalysis.methodAnalysisList.size})", methodTrees)
        )
    }

    private fun createMethodAnalysisTrees(methodAnalysisList: List<MethodAnalysis>): List<Tree> {
        return methodAnalysisList.mapIndexed { index, methodAnalysis ->
            createMethodAnalysisTree(index, methodAnalysis)
        }
    }

    private fun createMethodAnalysisTree(index: Int, methodAnalysis: MethodAnalysis): Tree {
        val javaMethod = methodAnalysis.signature.javaFormat(methodAnalysis.className, methodAnalysis.methodName)
        val compactMethod = methodAnalysis.signature.compactFormat(methodAnalysis.className, methodAnalysis.methodName)
        val invocationTrees = createInvocationAnalysisTrees(methodAnalysis.staticInvocations)
        val methodChildren = listOf(
            Tree("java: $javaMethod"),
            Tree("compact: $compactMethod"),
            Tree("complexity: ${methodAnalysis.complexity}"),
            Tree("categories: ${methodAnalysis.boundaryLogicCategories.joinToString(",")}"),
            Tree(
                "staticInvocations(${methodAnalysis.staticInvocations.size}): ${methodAnalysis.methodName}",
                invocationTrees
            ),
            Tree("isBoundaryLogic: ${methodAnalysis.isBoundaryLogic()}")
        )
        return Tree("Method[$index]", methodChildren)
    }

    private fun createInvocationAnalysisTrees(invocationAnalysisList: List<InvocationAnalysis>): List<Tree> {
        return invocationAnalysisList.mapIndexed { index, invocationAnalysis ->
            createInvocationAnalysisTree(index, invocationAnalysis)
        }
    }

    private fun createInvocationAnalysisTree(index: Int, invocationAnalysis: InvocationAnalysis): Tree {
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
        return Tree("Invocation[$index]", invocationNodes)
    }

    private fun createDisassemblyCommands(baseFileName: String, jvmClass: JvmClass): List<Command> {
        val treeList = format.classTreeList(jvmClass)
        val filePath = Path.of("$baseFileName-disassembled.txt")
        val disassemblyCommand = CreateFileCommand(filePath, treeList)
        return listOf(disassemblyCommand)
    }
}