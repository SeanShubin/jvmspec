package com.seanshubin.jvmspec.domain.infrastructure.command

interface CommandRunner {
    fun run(command: Command)
}
