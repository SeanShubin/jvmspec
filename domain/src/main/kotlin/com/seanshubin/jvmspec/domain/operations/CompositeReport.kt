package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.data.ClassFile
import java.nio.file.Path

class CompositeReport(val reports: List<Report>) : Report {
    override fun reportCommands(
        baseFileName: String,
        outputDir: Path,
        classFile: ClassFile
    ): List<Command> {
        return reports.flatMap { it.reportCommands(baseFileName, outputDir, classFile) }
    }
}
