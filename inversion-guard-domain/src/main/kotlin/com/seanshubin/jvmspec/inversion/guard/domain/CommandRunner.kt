package com.seanshubin.jvmspec.inversion.guard.domain

interface CommandRunner {
    fun runCommand(command: Command)
}
