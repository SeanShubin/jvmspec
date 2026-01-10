package com.seanshubin.jvmspec.inversion.guard.domain

interface Notifications {
    fun timeTakenMillis(millis: Long)
    fun executingCommand(command: Command)
}
