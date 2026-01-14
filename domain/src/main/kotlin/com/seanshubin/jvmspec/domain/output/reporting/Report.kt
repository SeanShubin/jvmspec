package com.seanshubin.jvmspec.domain.output.reporting

import com.seanshubin.jvmspec.domain.infrastructure.command.Command
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import java.nio.file.Path

interface Report {
    fun reportCommands(baseFileName: String, outputDir: Path, classFile: JvmClass): List<Command>
}
