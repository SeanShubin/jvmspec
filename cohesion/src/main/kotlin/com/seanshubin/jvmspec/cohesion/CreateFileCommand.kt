package com.seanshubin.jvmspec.cohesion

import java.nio.file.Path

data class CreateFileCommand(val path: Path, val lines: List<String>) : Command {
    override fun run(environment: Environment) {
        environment.files.createDirectories(path.parent)
        environment.files.write(path, lines)
    }
}
