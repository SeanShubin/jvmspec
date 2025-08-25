package com.seanshubin.jvmspec.domain.command

interface CommandRunner {
    fun run(command: Command)
}
