package com.seanshubin.jvmspec.domain.operations

import java.nio.file.Path

interface Notifications : ReportGenerator.Events {
    override fun processingFile(inputFile: Path, outputDir: Path)
    override fun timeTakenMillis(millis: Long)
}
