package com.seanshubin.jvmspec.inversion.guard.app

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.filter.Filter
import com.seanshubin.jvmspec.domain.filter.RegexFilter
import com.seanshubin.jvmspec.domain.format.JvmSpecFormat
import com.seanshubin.jvmspec.domain.format.JvmSpecFormatDetailed
import com.seanshubin.jvmspec.domain.stats.Stats
import com.seanshubin.jvmspec.domain.stats.StatsImpl
import com.seanshubin.jvmspec.domain.util.Timer
import com.seanshubin.jvmspec.inversion.guard.domain.*
import com.seanshubin.jvmspec.rules.CategoryRule
import com.seanshubin.jvmspec.rules.RuleInterpreter
import java.nio.file.Path
import java.time.Clock

class ApplicationDependencies(
    private val files: FilesContract,
    private val baseDir: Path,
    private val outputDir: Path,
    private val include: List<String>,
    private val exclude: List<String>,
    private val core: List<String>,
    private val boundary: List<String>,
    private val failOnUnknown: Boolean,
    private val categoryRuleSet: Map<String, CategoryRule>
) {
    private val clock: Clock = Clock.systemUTC()
    private val emit: (Any?) -> Unit = ::println
    private val notifications: Notifications = LineEmittingNotifications(emit)
    private val stats: Stats = StatsImpl()
    private val filter: Filter = RegexFilter(
        "class-file-name",
        mapOf(
            "include" to include,
            "exclude" to exclude
        ),
        stats::consumeMatchedFilterEvent,
        stats::consumeUnmatchedFilterEvent
    )
    private val fileSelector: FileSelector = FileSelectorImpl(baseDir, files, filter)
    private val jvmSpecFormat: JvmSpecFormat = JvmSpecFormatDetailed()
    private val ruleInterpreter: RuleInterpreter = RuleInterpreter(categoryRuleSet)
    private val coreBoundaryFilter: Filter = RegexFilter(
        "core-boundary",
        mapOf(
            "core" to core,
            "boundary" to boundary
        ),
        stats::consumeMatchedFilterEvent,
        stats::consumeUnmatchedFilterEvent
    )
    private val classAnalyzer: ClassAnalyzer = ClassAnalyzerImpl(
        coreBoundaryFilter,
        ruleInterpreter,
        failOnUnknown
    )
    private val classProcessor: ClassProcessor = ClassProcessorImpl(
        baseDir,
        outputDir,
        jvmSpecFormat
    )
    private val environment: Environment = EnvironmentImpl(files)
    private val commandRunner: CommandRunner = CommandRunnerImpl(
        environment,
        notifications::executingCommand
    )
    private val analysisSummarizer: AnalysisSummarizer = AnalysisSummarizerImpl(
        outputDir
    )

    private val statsSummarizer: StatsSummarizer = StatsSummarizerImpl(
        outputDir
    )

    private val timer: Timer = Timer(clock)
    val runner: Runnable = Runner(
        files,
        fileSelector,
        classAnalyzer,
        analysisSummarizer,
        statsSummarizer,
        stats,
        classProcessor,
        commandRunner,
        timer,
        notifications::timeTakenMillis
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
                configuration.failOnUnknown,
                configuration.categoryRuleSet
            ).runner
        }
    }
}