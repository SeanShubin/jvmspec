package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.files.FilesContract
import com.seanshubin.jvmspec.domain.files.FilesDelegate
import com.seanshubin.jvmspec.domain.format.JvmSpecFormat
import com.seanshubin.jvmspec.domain.format.JvmSpecFormatDetailed
import com.seanshubin.jvmspec.domain.util.FilterResult
import java.nio.file.Path
import java.nio.file.Paths

class Dependencies(
    private val args: Array<String>
) {
    private val baseDirName: String = args.getOrNull(0) ?: "."
    private val outputDirName: String = args.getOrNull(1) ?: "generated/cohesion"
    private val baseDir = Paths.get(baseDirName)
    private val outputDir = Paths.get(outputDirName)
    private val files: FilesContract = FilesDelegate
    private val whiteListPatterns: List<String> = listOf(""".*\.class""")
    private val blackListPatterns: List<String> = listOf()
    private val emit: (Any?) -> Unit = ::println
    private val notifications: Notifications = LineEmittingNotifications(emit)
    private val filter: (Path) -> FilterResult =
        FilterImpl(whiteListPatterns, blackListPatterns, notifications::filterEvent)
    private val fileSelector: FileSelector = FileSelectorImpl(baseDir, files, filter)
    private val jvmSpecFormat: JvmSpecFormat = JvmSpecFormatDetailed()
    private val classProcessor: ClassProcessor = ClassProcessorImpl(baseDir, outputDir, jvmSpecFormat)
    private val environment: Environment = EnvironmentImpl(files)
    private val commandRunner: CommandRunner = CommandRunnerImpl(environment)
    val runner: Runnable = Runner(
        files,
        fileSelector,
        classProcessor,
        commandRunner
    )
}
