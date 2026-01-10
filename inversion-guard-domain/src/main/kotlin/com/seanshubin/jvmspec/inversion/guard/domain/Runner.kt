package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.converter.toJvmClass
import com.seanshubin.jvmspec.domain.util.Timer

class Runner(
    private val files: FilesContract,
    private val fileSelector: FileSelector,
    private val classAnalyzer: ClassAnalyzer,
    private val analysisSummarizer: AnalysisSummarizer,
    private val classProcessor: ClassProcessor,
    private val commandRunner: CommandRunner,
    private val timer: Timer,
    private val timeTakenEvent: (Long) -> Unit
) : Runnable {
    override fun run() {
        val durationMillis = timer.withTimerMilliseconds {
            val analysisList = fileSelector.map { file ->
                val jvmClass = file.toJvmClass(files, file)
                classAnalyzer.analyzeClass(jvmClass)
            }
            val commands = analysisList.flatMap { analysis ->
                classProcessor.processClass(analysis)
            } + analysisSummarizer.summarize(analysisList)
            commands.forEach { command ->
                commandRunner.runCommand(command)
            }
        }
        timeTakenEvent(durationMillis)
    }
}
