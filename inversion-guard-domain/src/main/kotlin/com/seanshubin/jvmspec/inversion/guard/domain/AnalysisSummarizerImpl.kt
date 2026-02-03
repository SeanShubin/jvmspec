package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.infrastructure.collections.Tree
import java.nio.file.Path

class AnalysisSummarizerImpl(
    private val outputDir: Path,
) : AnalysisSummarizer {
    override fun summarize(analysisList: List<ClassAnalysis>): List<Command> {
        val sorted = analysisList.sortedWith(
            compareByDescending<ClassAnalysis>({
                it.countProblems()
            }).thenBy({
                it.jvmClass.thisClassName
            })
        )
        val reportTrees = sorted.map(::classReport)
        val path = outputDir.resolve("summary.txt")
        val createFileCommand = CreateFileCommand(path, reportTrees)
        return listOf(createFileCommand)
    }

    private fun classReport(analysis: ClassAnalysis): Tree {
        val problemCount = analysis.countProblems()
        val className = analysis.jvmClass.thisClassName
        val classComplexity = analysis.complexity()
        val methodNodes = analysis.methodAnalysisList.map(::methodReport)
        val problemString = if (problemCount == 1) "problem" else "problems"
        val header = "$problemCount $problemString in $className (complexity=$classComplexity)"
        return Tree(header, methodNodes)
    }

    private fun methodReport(methodAnalysis: MethodAnalysis): Tree {
        val javaSyntax = methodAnalysis.signature.javaFormat()
        val complexity = methodAnalysis.complexity
        val boundaryString = if (methodAnalysis.boundaryLogicCategories.isEmpty()) {
            "does not match category allowed at boundary"
        } else {
            "allowed at boundary because of categories: ${
                methodAnalysis.boundaryLogicCategories.joinToString(
                    ", ",
                    "(",
                    ")"
                )
            }"
        }
        val header = "complexity=$complexity, $javaSyntax $boundaryString"
        val invocationNodes = methodAnalysis.staticInvocations.map(::invocationReport)
        return Tree(header, invocationNodes)
    }

    private fun invocationReport(invocationAnalysis: InvocationAnalysis): Tree {
        val compactSyntax =
            invocationAnalysis.signature.compactFormat()
        val opcodeName = invocationAnalysis.invocationOpcodeName
        val invocationTypeName = invocationAnalysis.invocationType.name
        return Tree("$invocationTypeName $opcodeName $compactSyntax")
    }
}
