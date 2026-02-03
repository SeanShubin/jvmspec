package com.seanshubin.jvmspec.infrastructure.command

import java.nio.file.Path

class CreateDirectories(val directories: Path) : Command {
    override fun execute(environment: Environment) {
        environment.files.createDirectories(directories)
    }
}