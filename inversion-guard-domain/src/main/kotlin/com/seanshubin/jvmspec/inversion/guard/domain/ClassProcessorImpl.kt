package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.infrastructure.filesystem.PathUtil.removeExtension
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.output.formatting.JvmSpecFormat
import java.nio.file.Path

class ClassProcessorImpl(
    private val baseDir: Path,
    private val outputDir: Path,
    private val format: JvmSpecFormat
) : ClassProcessor {
    override fun processClass(classAnalysis: ClassAnalysis): List<Command> {
        val relativePath = baseDir.relativize(classAnalysis.jvmClass.origin)
        val baseFileName = outputDir.resolve(relativePath).removeExtension("class")
        val disassemblyCommands = createDisassemblyCommands(baseFileName, classAnalysis.jvmClass)
        return disassemblyCommands
    }

    private fun createDisassemblyCommands(baseFileName: String, jvmClass: JvmClass): List<Command> {
        val treeList = format.classTreeList(jvmClass)
        val filePath = Path.of("$baseFileName-disassembled.txt")
        val disassemblyCommand = CreateFileCommand(filePath, treeList)
        return listOf(disassemblyCommand)
    }
}