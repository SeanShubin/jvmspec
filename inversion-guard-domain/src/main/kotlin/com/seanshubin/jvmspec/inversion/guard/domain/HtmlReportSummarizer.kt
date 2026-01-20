package com.seanshubin.jvmspec.inversion.guard.domain

interface HtmlReportSummarizer {
    fun summarize(analysisList: List<ClassAnalysis>): List<Command>
}
