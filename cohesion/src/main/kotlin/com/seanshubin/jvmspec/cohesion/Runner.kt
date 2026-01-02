package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.converter.toJvmClass
import com.seanshubin.jvmspec.domain.files.FilesContract

class Runner(
    private val files: FilesContract,
    private val fileSelector: FileSelector,
    private val classProcessor: ClassProcessor,
    private val commandRunner: CommandRunner
) : Runnable {
    override fun run() {
        fileSelector.flatMap { file ->
            val jvmClass = file.toJvmClass(files, file)
            classProcessor.processClass(jvmClass)
        }.forEach { command ->
            commandRunner.runCommand(command)
        }
    }
}
