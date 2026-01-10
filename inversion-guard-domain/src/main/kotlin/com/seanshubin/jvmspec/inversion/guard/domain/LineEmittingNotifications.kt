package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.durationformat.DurationFormat

class LineEmittingNotifications(
    private val emit: (Any?) -> Unit
) : Notifications {
    override fun timeTakenMillis(millis: Long) {
        DurationFormat.milliseconds.format(millis).let { formattedDuration ->
            emit("Time taken: $formattedDuration")
        }
    }

    override fun executingCommand(command: Command) {
        when (command) {
            is CreateFileCommand -> emit("Create file ${command.path}")
        }
    }
}
