package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.format.JvmSpecFormat
import com.seanshubin.jvmspec.domain.format.JvmSpecFormatDetailed
import com.seanshubin.jvmspec.domain.util.FilterResult
import com.seanshubin.jvmspec.rules.CategoryRule
import java.nio.file.Path

class ApplicationDependencies(
    private val files: FilesContract,
    private val baseDir: Path,
    private val outputDir: Path,
    private val include: List<String>,
    private val exclude: List<String>,
    private val core: List<String>,
    private val boundary: List<String>,
    private val categoryRuleSet: Map<String, CategoryRule>
) {
    private val emit: (Any?) -> Unit = ::println
    private val notifications: Notifications = LineEmittingNotifications(emit)
    private val filter: (Path) -> FilterResult =
        FilterImpl(include, exclude, notifications::filterEvent)
    private val fileSelector: FileSelector = FileSelectorImpl(baseDir, files, filter)
    private val jvmSpecFormat: JvmSpecFormat = JvmSpecFormatDetailed()
    private val classProcessor: ClassProcessor = ClassProcessorImpl(
        baseDir,
        outputDir,
        jvmSpecFormat,
        core,
        boundary,
        categoryRuleSet
    )
    private val environment: Environment = EnvironmentImpl(files)
    private val commandRunner: CommandRunner = CommandRunnerImpl(environment)
    val runner: Runnable = Runner(
        files,
        fileSelector,
        classProcessor,
        commandRunner
    )

    companion object {
        fun fromConfiguration(files: FilesContract, configuration: Configuration): Runnable {
            return ApplicationDependencies(
                files,
                configuration.baseDir,
                configuration.outputDir,
                configuration.include,
                configuration.exclude,
                configuration.core,
                configuration.boundary,
                configuration.categoryRuleSet
            ).runner
        }
    }
}