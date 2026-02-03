package com.seanshubin.jvmspec.output.reporting

import com.seanshubin.jvmspec.infrastructure.command.Command
import com.seanshubin.jvmspec.model.api.JvmClass
import java.nio.file.Path

interface Report {
    fun reportCommands(baseFileName: String, outputDir: Path, classFile: JvmClass): List<Command>
}
