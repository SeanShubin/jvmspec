package com.seanshubin.jvmspec.inversion.guard.domain

import java.nio.file.Path

class QualityMetricsDetailSummarizerImpl(
    private val outputDir: Path
) : QualityMetricsDetailSummarizer {
    override fun summarize(analysisList: List<ClassAnalysis>): List<Command> {
        val classDetails = analysisList
            .filter { it.countProblems() > 0 }
            .map { classAnalysis ->
                val methodDetails = classAnalysis.methodAnalysisList
                    .filter { !it.isBoundaryLogic() && it.staticInvocations.isNotEmpty() }
                    .map { methodAnalysis ->
                        val invocationDetails = methodAnalysis.staticInvocations
                            .map { invocation ->
                                QualityMetricsInvocationDetail(
                                    invocationType = invocation.invocationType.name,
                                    opcode = invocation.invocationOpcodeName,
                                    className = invocation.className,
                                    methodName = invocation.methodName,
                                    signature = invocation.signature.compactFormat()
                                )
                            }

                        QualityMetricsMethodDetail(
                            methodSignature = formatMethodSignature(methodAnalysis),
                            complexity = methodAnalysis.complexity,
                            isBoundaryLogic = methodAnalysis.isBoundaryLogic(),
                            boundaryLogicCategories = methodAnalysis.boundaryLogicCategories.sorted(),
                            invocations = invocationDetails
                        )
                    }
                    .sortedBy { it.methodSignature }

                QualityMetricsClassDetail(
                    className = classAnalysis.jvmClass.thisClassName,
                    problemCount = classAnalysis.countProblems(),
                    complexity = classAnalysis.complexity(),
                    methods = methodDetails
                )
            }
            .sortedBy { it.className }

        val report = QualityMetricsDetailReport(classes = classDetails)
        val path = outputDir.resolve("quality-metrics-staticInvocationsThatShouldBeInverted.json")
        val command = CreateJsonFileCommand(path, report)
        return listOf(command)
    }

    private fun formatMethodSignature(methodAnalysis: MethodAnalysis): String {
        return methodAnalysis.signature.javaFormat()
    }
}
