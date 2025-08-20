package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.durationformat.DurationFormat
import java.nio.file.Path

class LineEmittingNotifications(val emit: (Any?) -> Unit) : Notifications {
    override fun processingFile(inputFile: Path, outputDir: Path) {
        emit("$inputFile -> $outputDir")
    }

    override fun timeTakenMillis(millis: Long) {
        DurationFormat.milliseconds.format(millis).let { formattedDuration ->
            emit("Time taken: $formattedDuration")
        }
    }
}
