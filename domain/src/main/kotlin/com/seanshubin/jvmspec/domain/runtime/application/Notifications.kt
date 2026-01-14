package com.seanshubin.jvmspec.domain.runtime.application

import java.nio.file.Path

interface Notifications : ReportGenerator.Events {
    override fun processingFile(inputFile: Path, outputDir: Path)
    override fun timeTakenMillis(millis: Long)
}
