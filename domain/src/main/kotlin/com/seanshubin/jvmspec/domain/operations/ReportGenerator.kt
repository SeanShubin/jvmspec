package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.command.CreateDirectories
import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.jvmimpl.JvmClassImpl
import com.seanshubin.jvmspec.domain.util.FilterResult
import com.seanshubin.jvmspec.domain.util.RegexUtil
import java.io.DataInputStream
import java.nio.file.Path
import java.time.Clock

class ReportGenerator(
    private val inputDir: Path,
    private val outputDir: Path,
    private val include: List<String>,
    private val exclude: List<String>,
    private val files: FilesContract,
    private val clock: Clock,
    private val events: Events,
    private val disassembleReport: Report
) : Runnable {
    override fun run() {
        withTimer {
            val acceptFile = RegexUtil.createMatchFunctionFromList(include, exclude)
            val acceptFileBoolean = { file: Path ->
                val fileName = file.toString()
                val result = acceptFile(fileName)
                result == FilterResult.WHITELIST_ONLY
            }
            files.walk(inputDir).forEach { inputFile ->
                if (acceptFileBoolean(inputFile)) {
                    processFile(disassembleReport, inputDir, outputDir, inputFile)
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

    fun processFile(report: Report, baseInputDir: Path, baseOutputDir: Path, inputFile: Path) {
        val relativePath = baseInputDir.relativize(inputFile)
        val fileName = inputFile.fileName.toString()
        val outputDir = baseOutputDir.resolve(relativePath).parent
        events.processingFile(inputFile, outputDir)
        val javaFile = files.newInputStream(inputFile).use { inputStream ->
            val input = DataInputStream(inputStream)
            ClassFile.fromDataInput(inputFile, input)
        }
        val jvmClass = JvmClassImpl(javaFile)
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
