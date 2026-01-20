package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.analysis.statistics.Stats
import com.seanshubin.jvmspec.domain.infrastructure.time.Timer
import com.seanshubin.jvmspec.domain.model.conversion.Converter

class Runner(
    private val files: FilesContract,
    private val fileSelector: FileSelector,
    private val classAnalyzer: ClassAnalyzer,
    private val analysisSummarizer: AnalysisSummarizer,
    private val statsSummarizer: StatsSummarizer,
    private val qualityMetricsSummarizer: QualityMetricsSummarizer,
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
            } + analysisSummarizer.summarize(analysisList) + statsSummarizer.summarize(stats) + qualityMetricsSummarizer.summarize(
                analysisList
            )
            commands.forEach { command ->
                commandRunner.runCommand(command)
            }
        }
        timeTakenEvent(durationMillis)
    }
}
