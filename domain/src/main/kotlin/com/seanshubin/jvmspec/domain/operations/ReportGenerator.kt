package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.files.FilesContract
import java.io.DataInputStream
import java.nio.file.Path
import java.time.Clock

class ReportGenerator(
    private val args: Array<String>,
    private val files: FilesContract,
    private val clock: Clock,
    private val events: Events,
    private val report: Report
) : Runnable {
    override fun run() {
        withTimer {
            if (args.size != 2) {
                throw IllegalArgumentException("Expected 2 arguments: <inputDir> <outputDir>")
            }
            val inputDir = Path.of(args[0])
            val outputDir = Path.of(args[1])
            files.walk(inputDir).forEach { inputFile ->
                if (accept(inputFile)) {
                    processFile(inputDir, outputDir, inputFile)
                }
            }
        }
    }

    fun withTimer(f: () -> Unit) {
        val startTime = clock.millis()
        f()
        val endTime = clock.millis()
        val durationMillis = endTime - startTime
        events.timeTakenMillis(durationMillis)
    }

    fun accept(file: Path): Boolean {
        return file.toString().endsWith(".class")
    }

    fun processFile(baseInputDir: Path, baseOutputDir: Path, inputFile: Path) {
        val relativePath = baseInputDir.relativize(inputFile)
        val fileName = inputFile.fileName.toString()
        val outputDir = baseOutputDir.resolve(relativePath).parent
        events.processingFile(inputFile, outputDir)
        val javaFile = files.newInputStream(inputFile).use { inputStream ->
            val input = DataInputStream(inputStream)
            ClassFile.fromDataInput(input)
        }
        report.reportCommands(fileName, outputDir, javaFile).forEach { command ->
            events.executeCommand(command)
        }
    }

    interface Events {
        fun processingFile(inputFile: Path, outputDir: Path)
        fun timeTakenMillis(millis: Long)
        fun executeCommand(command: Command)
    }
}