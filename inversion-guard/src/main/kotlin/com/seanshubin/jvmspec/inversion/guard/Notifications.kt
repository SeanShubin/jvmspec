package com.seanshubin.jvmspec.inversion.guard

interface Notifications {
    fun timeTakenMillis(millis: Long)
    fun executingCommand(command: Command)
}
