package com.seanshubin.jvmspec.inversion.guard.domain

interface AnalysisSummarizer {
    fun summarize(analysisList: List<ClassAnalysis>): List<Command>
}
