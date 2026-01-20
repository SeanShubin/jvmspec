package com.seanshubin.jvmspec.inversion.guard.domain

interface QualityMetricsSummarizer {
    fun summarize(analysisList: List<ClassAnalysis>): List<Command>
}
