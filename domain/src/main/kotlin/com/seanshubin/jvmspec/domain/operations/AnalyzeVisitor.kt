package com.seanshubin.jvmspec.domain.operations

import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.FileVisitor
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributes

class AnalyzeVisitor(private val inputDir: Path, private val outputDir: Path) : FileVisitor<Path> {
    override fun preVisitDirectory(
        dir: Path?,
        attrs: BasicFileAttributes
    ): FileVisitResult {
        return FileVisitResult.CONTINUE
    }

    override fun visitFile(
        file: Path,
        attrs: BasicFileAttributes
    ): FileVisitResult {
        Analyzer.processFile(inputDir, outputDir, file)
        return FileVisitResult.CONTINUE
    }

    override fun visitFileFailed(file: Path?, exc: IOException): FileVisitResult {
        return FileVisitResult.CONTINUE
    }

    override fun postVisitDirectory(
        dir: Path?,
        exc: IOException?
    ): FileVisitResult {
        return FileVisitResult.CONTINUE
    }
}