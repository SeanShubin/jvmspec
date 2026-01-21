package com.seanshubin.jvmspec.inversion.guard.domain

import java.nio.file.Path

class QualityMetricsDetailSummarizerImpl(
    private val outputDir: Path,
    private val reportGenerator: QualityMetricsDetailReportGenerator
) : QualityMetricsDetailSummarizer {
    override fun summarize(analysisList: List<ClassAnalysis>): List<Command> {
        val report = reportGenerator.generate(analysisList)
        val path = outputDir.resolve("quality-metrics-staticInvocationsThatShouldBeInverted.json")
        val command = CreateJsonFileCommand(path, report)
        return listOf(command)
    }
}
