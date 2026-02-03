package com.seanshubin.jvmspec.output.reporting

import com.seanshubin.jvmspec.infrastructure.command.Command
import com.seanshubin.jvmspec.model.api.JvmClass
import java.nio.file.Path

class CompositeReport(val reports: List<Report>) : Report {
    override fun reportCommands(
        baseFileName: String,
        outputDir: Path,
        classFile: JvmClass
    ): List<Command> {
        return reports.flatMap { it.reportCommands(baseFileName, outputDir, classFile) }
    }
}
