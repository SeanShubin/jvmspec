package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.command.Command
import java.nio.file.Path

class CompositeReport(val reports: List<Report>) : Report {
    override fun reportCommands(
        baseFileName: String,
        outputDir: Path,
        classFile: ApiClass
    ): List<Command> {
        return reports.flatMap { it.reportCommands(baseFileName, outputDir, classFile) }
    }
}
