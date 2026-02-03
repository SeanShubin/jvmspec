package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.analysis.statistics.Stats
import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.infrastructure.time.Timer
import com.seanshubin.jvmspec.model.conversion.Converter

class Runner(
    private val files: FilesContract,
    private val fileSelector: FileSelector,
    private val classAnalyzer: ClassAnalyzer,
    private val qualityMetricsSummarizer: QualityMetricsSummarizer,
    private val qualityMetricsDetailSummarizer: QualityMetricsDetailSummarizer,
    private val htmlReportSummarizer: HtmlReportSummarizer,
    private val htmlStatsSummarizer: HtmlStatsSummarizer,
    private val stats: Stats,
    private val classProcessor: ClassProcessor,
    private val commandRunner: CommandRunner,
    private val timer: Timer,
    private val timeTakenEvent: (Long) -> Unit,
    private val converter: Converter
) : Runnable {
    override fun run() {
        val durationMillis = timer.withTimerMilliseconds {
            val analysisList = fileSelector.map { file ->
                val jvmClass = with(converter) { file.toJvmClass(files, file) }
                classAnalyzer.analyzeClass(jvmClass)
            }
            val commands = analysisList.flatMap { analysis ->
                classProcessor.processClass(analysis)
            } + qualityMetricsSummarizer.summarize(
                analysisList
            ) + qualityMetricsDetailSummarizer.summarize(analysisList) + htmlReportSummarizer.summarize(analysisList) + htmlStatsSummarizer.summarize(
                stats
            )
            commands.forEach { command ->
                commandRunner.runCommand(command)
            }
        }
        timeTakenEvent(durationMillis)
    }
}
