package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.composition.Application
import com.seanshubin.jvmspec.infrastructure.time.DurationFormat
import java.nio.file.Files
import java.nio.file.Paths

object StressTest {
    @JvmStatic
    fun main(args: Array<String>) {
        val start = System.currentTimeMillis()
        val baseDir = Paths.get(args[0])
        val inputDir = baseDir.resolve(args[1])
        val outputDir = baseDir.resolve(args[2])
        var decompiledCount = 0
        Files.walk(inputDir).filter {
            it.fileName.toString().endsWith(".class")
        }.forEach { inputPath ->
            val relativePath = inputDir.relativize(inputPath)
            val outputPath = outputDir.resolve(relativePath).resolveSibling(relativePath.fileName.toString() + ".txt")
            val args = arrayOf(inputPath.toString())
            val integrations = StressTestIntegrations(args, outputPath)
            Application.execute(integrations)
            integrations.close()
            decompiledCount++
        }
        val end = System.currentTimeMillis()
        val duration = end - start
        val durationString = DurationFormat.milliseconds.format(duration)
        val decompiledCountString = String.format("%,d", decompiledCount)
        println("decompiled $decompiledCountString classes in $durationString")
    }
}
