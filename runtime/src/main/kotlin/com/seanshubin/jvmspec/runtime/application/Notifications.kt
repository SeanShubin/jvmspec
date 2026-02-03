package com.seanshubin.jvmspec.runtime.application

import java.nio.file.Path

interface Notifications : ReportGenerator.Events {
    override fun processingFile(inputFile: Path, outputDir: Path)
    override fun timeTakenMillis(millis: Long)
}
