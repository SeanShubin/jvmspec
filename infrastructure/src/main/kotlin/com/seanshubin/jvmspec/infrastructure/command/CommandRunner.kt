package com.seanshubin.jvmspec.infrastructure.command

interface CommandRunner {
    fun run(command: Command)
}
