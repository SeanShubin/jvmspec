package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.json.JsonMappers
import java.io.DataInputStream
import java.nio.file.Files
import java.nio.file.Path

object Analyzer {
    fun accept(file: Path): Boolean {
        return file.toString().endsWith(".class")
    }

    fun processFile(baseInputDir: Path, baseOutputDir: Path, inputFile: Path) {
        if (!accept(inputFile)) return
        val relativePath = baseInputDir.relativize(inputFile)
        val fileName = inputFile.fileName.toString()
        val outputDir = baseOutputDir.resolve(relativePath).parent
        Files.createDirectories(outputDir)
        val outputFileName = "${fileName}.json"
        val outputFile = outputDir.resolve(outputFileName)
        println("$inputFile -> $outputFile")
        val javaFile = Files.newInputStream(inputFile).use { inputStream ->
            val dataInput = DataInputStream(inputStream)
            ClassFile.fromDataInput(dataInput)
        }
        val json = JsonMappers.pretty.writeValueAsString(javaFile)
        Files.writeString(outputFile, json)
    }
}