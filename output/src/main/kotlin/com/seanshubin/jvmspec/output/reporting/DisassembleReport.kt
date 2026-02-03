package com.seanshubin.jvmspec.output.reporting

import com.seanshubin.jvmspec.infrastructure.command.Command
import com.seanshubin.jvmspec.infrastructure.command.WriteLines
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.output.formatting.JvmSpecFormat
import java.nio.file.Path

class DisassembleReport(private val format: JvmSpecFormat) : Report {
    override fun reportCommands(baseFileName: String, outputDir: Path, classFile: JvmClass): List<Command> {
        val treeList = format.classTreeList(classFile)
        val lines = treeList.flatMap { tree -> tree.toLines { line -> "  $line" } }
        val filePath = outputDir.resolve("$baseFileName-disassembled.txt")
        val generateReportCommand = WriteLines(filePath, lines)
        return listOf(generateReportCommand)
    }
}
