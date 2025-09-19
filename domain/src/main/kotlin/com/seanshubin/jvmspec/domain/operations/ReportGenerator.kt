package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.aggregation.AggregateData
import com.seanshubin.jvmspec.domain.aggregation.AggregatorImpl
import com.seanshubin.jvmspec.domain.aggregation.QualifiedMethod
import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.command.CreateDirectories
import com.seanshubin.jvmspec.domain.data.ClassFile
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
    private val includeMethod: List<String>,
    private val excludeMethod: List<String>,
    private val includeClass: List<String>,
    private val excludeClass: List<String>,
    private val files: FilesContract,
    private val clock: Clock,
    private val events: Events,
    private val disassembleReport: Report,
) : Runnable {
    override fun run() {
        withTimer {
            val acceptMethodKey = RegexUtil.createMatchFunctionFromList(includeMethod, excludeMethod)
            val acceptMethod = { qualifiedMethod: QualifiedMethod ->
                acceptMethodKey(qualifiedMethod.regexMatchKey())
            }
            val acceptClass = RegexUtil.createMatchFunctionFromList(includeClass, excludeClass)
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
                if (accept(inputFile)) {
                    processFile(compositeReport, inputDir, outputDir, inputFile)
                }
            }
            files.write(newFile(outputDir, "summary-static.txt"), aggregator.summaryDescendingStaticReferenceCount())
            files.write(
                newFile(outputDir, "summary-complexity.txt"),
                aggregator.summaryDescendingCyclomaticComplexity()
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

    fun accept(file: Path): Boolean {
        return file.toString().endsWith(".class")
    }

    fun processFile(report: Report, baseInputDir: Path, baseOutputDir: Path, inputFile: Path) {
        val relativePath = baseInputDir.relativize(inputFile)
        val fileName = inputFile.fileName.toString()
        val outputDir = baseOutputDir.resolve(relativePath).parent
        events.processingFile(inputFile, outputDir)
        val javaFile = files.newInputStream(inputFile).use { inputStream ->
            val input = DataInputStream(inputStream)
            ClassFile.fromDataInput(input)
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
