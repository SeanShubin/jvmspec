package com.seanshubin.jvmspec.inversion.guard.domain

import java.nio.file.Path

class QualityMetricsSummarizerImpl(
    private val outputDir: Path
) : QualityMetricsSummarizer {
    override fun summarize(analysisList: List<ClassAnalysis>): List<Command> {
        val count = countBoundaryInvocationsInNonBoundaryMethods(analysisList)
        val metrics = QualityMetrics(
            staticInvocationsThatShouldBeInverted = count
        )
        val path = outputDir.resolve("quality-metrics.json")
        val command = CreateJsonFileCommand(path, metrics)
        return listOf(command)
    }

    private fun countBoundaryInvocationsInNonBoundaryMethods(
        analysisList: List<ClassAnalysis>
    ): Int {
        return analysisList.sumOf { classAnalysis ->
            classAnalysis.methodAnalysisList
                .filter { !it.isBoundaryLogic() }
                .sumOf { methodAnalysis ->
                    methodAnalysis.staticInvocations.count { invocation ->
                        invocation.invocationType == InvocationType.BOUNDARY
                    }
                }
        }
    }
}
