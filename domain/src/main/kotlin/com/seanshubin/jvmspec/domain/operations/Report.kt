package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.command.Command
import com.seanshubin.jvmspec.domain.jvm.JvmClass
import java.nio.file.Path

interface Report {
    fun reportCommands(baseFileName: String, outputDir: Path, classFile: JvmClass): List<Command>
}
