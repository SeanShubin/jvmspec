package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.aggregation.AggregateData
import com.seanshubin.jvmspec.domain.aggregation.AggregatorImpl
import com.seanshubin.jvmspec.domain.aggregation.QualifiedMethod
import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.command.CreateDirectories
import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.data.OriginClassFile
import com.seanshubin.jvmspec.domain.files.FilesContract
import com.seanshubin.jvmspec.domain.util.MatchEnum
import com.seanshubin.jvmspec.domain.util.RegexUtil
import java.io.DataInputStream
import java.nio.file.Files
import java.nio.file.Path
import java.time.Clock

class ReportGenerator(
    private val inputDir: Path,
    private val outputDir: Path,
    private val include: List<String>,
    private val exclude: List<String>,
    private val methodWhitelist: List<String>,
    private val methodBlacklist: List<String>,
    private val classWhitelist: List<String>,
    private val classBlacklist: List<String>,
    private val files: FilesContract,
    private val clock: Clock,
    private val events: Events,
    private val disassembleReport: Report,
) : Runnable {
    override fun run() {
        withTimer {
            val acceptFile = RegexUtil.createMatchFunctionFromList(include, exclude)
            val acceptFileBoolean = { file: Path ->
                val fileName = file.toString()
                val result = acceptFile(fileName)
                result == MatchEnum.WHITELIST_ONLY
            }
            val acceptMethodKey = RegexUtil.createMatchFunctionFromList(methodWhitelist, methodBlacklist)
            val acceptMethod = { qualifiedMethod: QualifiedMethod ->
                acceptMethodKey(qualifiedMethod.regexMatchKey())
            }
            val acceptClass = RegexUtil.createMatchFunctionFromList(classWhitelist, classBlacklist)
            val initialAggregateData = AggregateData.create(acceptMethod, acceptClass)
            val aggregator = AggregatorImpl(initialAggregateData)
            val methodReport: Report = MethodReport(aggregator)
            val compositeReport: Report = CompositeReport(
                listOf(
                    disassembleReport,
                    methodReport
                )
            )
            files.walk(inputDir).forEach { inputFile ->
                if (acceptFileBoolean(inputFile)) {
                    processFile(compositeReport, inputDir, outputDir, inputFile)
                }
            }
            files.write(newFile(outputDir, "summary-static.txt"), aggregator.summaryDescendingStaticReferenceCount())
            files.write(
                newFile(outputDir, "summary-complexity.txt"),
                aggregator.summaryDescendingCyclomaticComplexity()
            )
            files.write(
                newFile(outputDir, "summary-origin.txt"),
                aggregator.summaryOrigin()
            )
            MatchEnum.entries.forEach { matchEnum ->
                files.write(
                    newFile(outputDir, "summary-methods-$matchEnum.txt"),
                    aggregator.summaryMethodNames(matchEnum)
                )
                files.write(
                    newFile(outputDir, "summary-classes-$matchEnum.txt"),
                    aggregator.summaryClassNames(matchEnum)
                )
            }
        }
    }

    private fun newFile(dir: Path, name: String): Path {
        val file = dir.resolve(name)
        Files.deleteIfExists(file)
        return file
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
            val origin = OriginClassFile(inputFile)
            ClassFile.fromDataInput(origin, input)
        }
        events.executeCommand(CreateDirectories(outputDir))
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
