package com.seanshubin.jvmspec.runtime.application

import com.seanshubin.jvmspec.infrastructure.command.Command
import com.seanshubin.jvmspec.infrastructure.command.CommandRunner
import com.seanshubin.jvmspec.infrastructure.time.DurationFormat
import java.nio.file.Path

class LineEmittingNotifications(
    val emit: (Any?) -> Unit,
    val commandRunner: CommandRunner
) : Notifications {
    override fun processingFile(inputFile: Path, outputDir: Path) {
        emit("$inputFile -> $outputDir")
    }

    override fun timeTakenMillis(millis: Long) {
        DurationFormat.milliseconds.format(millis).let { formattedDuration ->
            emit("Time taken: $formattedDuration")
        }
    }

    override fun executeCommand(command: Command) {
        commandRunner.run(command)
    }
}
