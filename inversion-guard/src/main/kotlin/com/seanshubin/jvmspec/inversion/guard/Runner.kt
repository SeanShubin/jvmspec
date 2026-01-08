package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.converter.toJvmClass

class Runner(
    private val files: FilesContract,
    private val fileSelector: FileSelector,
    private val classAnalyzer: ClassAnalyzer,
    private val analysisSummarizer: AnalysisSummarizer,
    private val classProcessor: ClassProcessor,
    private val commandRunner: CommandRunner
) : Runnable {
    override fun run() {
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
}
