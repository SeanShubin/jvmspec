package com.seanshubin.jvmspec.inversion.guard

interface AnalysisSummarizer {
    fun summarize(analysisList: List<ClassAnalysis>): List<Command>
}
