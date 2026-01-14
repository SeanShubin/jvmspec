package com.seanshubin.jvmspec.domain.runtime.application

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.analysis.filtering.Filter
import com.seanshubin.jvmspec.domain.infrastructure.command.Command
import com.seanshubin.jvmspec.domain.infrastructure.command.CreateDirectories
import com.seanshubin.jvmspec.domain.infrastructure.time.Timer
import com.seanshubin.jvmspec.domain.model.conversion.Converter.toJvmClass
import com.seanshubin.jvmspec.domain.output.reporting.Report
import java.nio.file.Path

class ReportGenerator(
    private val inputDir: Path,
    private val outputDir: Path,
    private val classFileNameFilter: Filter,
    private val files: FilesContract,
    private val timer: Timer,
    private val events: Events,
    private val disassembleReport: Report
) : Runnable {
    override fun run() {
        val durationMillis = timer.withTimerMilliseconds {
            val acceptFileBoolean = { file: Path ->
                val fileName = file.toString()
                val result = classFileNameFilter.match(fileName)
                result == setOf("include")
            }
            files.walk(inputDir).forEach { inputFile ->
                if (acceptFileBoolean(inputFile)) {
                    processFile(disassembleReport, inputDir, outputDir, inputFile)
                }
            }
        }
        events.timeTakenMillis(durationMillis)
    }

    fun processFile(report: Report, baseInputDir: Path, baseOutputDir: Path, inputFile: Path) {
        val relativePath = baseInputDir.relativize(inputFile)
        val fileName = inputFile.fileName.toString()
        val outputDir = baseOutputDir.resolve(relativePath).parent
        events.processingFile(inputFile, outputDir)
        val jvmClass = inputFile.toJvmClass(files, inputFile)
        events.executeCommand(CreateDirectories(outputDir))
        report.reportCommands(fileName, outputDir, jvmClass).forEach { command ->
            events.executeCommand(command)
        }
    }

    interface Events {
        fun processingFile(inputFile: Path, outputDir: Path)
        fun timeTakenMillis(millis: Long)
        fun executeCommand(command: Command)
    }
}
