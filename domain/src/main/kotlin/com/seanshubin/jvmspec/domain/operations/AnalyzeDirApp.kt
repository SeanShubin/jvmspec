package com.seanshubin.jvmspec.domain.operations

import java.nio.file.Files
import java.nio.file.Paths

object AnalyzeDirApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputDir = Paths.get(args[0])
        val outputDir = Paths.get(args[1])
        val analyzeVisitor = AnalyzeVisitor(inputDir, outputDir)
        Files.walkFileTree(inputDir, analyzeVisitor)
    }
}
