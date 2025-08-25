package com.seanshubin.jvmspec.domain.command

import java.nio.file.Path

data class WriteLines(
    val path: Path,
    val lines: List<String>
) : Command {
    override fun execute(environment: Environment) {
        environment.files.write(path, lines)
    }
}