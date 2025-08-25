package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.command.CreateDirectories
import com.seanshubin.jvmspec.domain.command.WriteLines
import com.seanshubin.jvmspec.domain.data.ClassFile
import java.nio.file.Path

class DisassembleReport : Report {
    override fun reportCommands(baseFileName: String, outputDir: Path, classFile: ClassFile): List<Command> {
        val lines = classFile.lines()
        val createDirectoriesCommand = CreateDirectories(outputDir)
        val filePath = outputDir.resolve("$baseFileName-disassembled.txt")
        val generateReportCommand = WriteLines(filePath, lines)
        return listOf(createDirectoriesCommand, generateReportCommand)
    }
}
