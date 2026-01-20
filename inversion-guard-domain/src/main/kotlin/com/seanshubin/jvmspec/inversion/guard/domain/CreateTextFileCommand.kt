package com.seanshubin.jvmspec.inversion.guard.domain

import java.nio.file.Path

data class CreateTextFileCommand(
    val path: Path,
    val content: String
) : Command {
    override fun run(environment: Environment) {
        environment.files.createDirectories(path.parent)
        environment.files.writeString(path, content)
    }
}
