package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.command.WriteLines
import java.nio.file.Path

class DisassembleReport : Report {
    override fun reportCommands(baseFileName: String, outputDir: Path, classFile: ApiClass): List<Command> {
        val lines = classFile.disassemblyLines()
        val filePath = outputDir.resolve("$baseFileName-disassembled.txt")
        val generateReportCommand = WriteLines(filePath, lines)
        return listOf(generateReportCommand)
    }
}
