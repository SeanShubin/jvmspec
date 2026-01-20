package com.seanshubin.jvmspec.inversion.guard.domain

interface QualityMetricsDetailSummarizer {
    fun summarize(analysisList: List<ClassAnalysis>): List<Command>
}
