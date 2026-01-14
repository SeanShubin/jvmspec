package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.infrastructure.collections.Tree
import java.nio.file.Path

data class CreateFileCommand(val path: Path, val roots: List<Tree>) : Command {
    override fun run(environment: Environment) {
        environment.files.createDirectories(path.parent)
        val lines = roots.flatMap { tree -> tree.toLines { line -> "  $line" } }
        environment.files.write(path, lines)
    }
}
