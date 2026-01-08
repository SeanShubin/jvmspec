package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.converter.toJvmClass

class Runner(
    private val files: FilesContract,
    private val fileSelector: FileSelector,
    private val classAnalyzer: ClassAnalyzer,
    private val classProcessor: ClassProcessor,
    private val commandRunner: CommandRunner
) : Runnable {
    override fun run() {
        fileSelector.flatMap { file ->
            val jvmClass = file.toJvmClass(files, file)
            val analysis = classAnalyzer.analyzeClass(jvmClass)
            classProcessor.processClass(jvmClass, analysis)
        }.forEach { command ->
            commandRunner.runCommand(command)
        }
    }
}
