package com.seanshubin.jvmspec.inversion.guard.app

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.domain.analysis.filtering.Filter
import com.seanshubin.jvmspec.domain.analysis.filtering.RegexFilter
import com.seanshubin.jvmspec.domain.analysis.statistics.Stats
import com.seanshubin.jvmspec.domain.analysis.statistics.StatsImpl
import com.seanshubin.jvmspec.domain.infrastructure.time.Timer
import com.seanshubin.jvmspec.domain.model.api.JvmAttributeFactory
import com.seanshubin.jvmspec.domain.model.api.JvmClassFactory
import com.seanshubin.jvmspec.domain.model.api.JvmFieldFactory
import com.seanshubin.jvmspec.domain.model.api.JvmMethodFactory
import com.seanshubin.jvmspec.domain.model.conversion.Converter
import com.seanshubin.jvmspec.domain.model.implementation.JvmAttributeFactoryImpl
import com.seanshubin.jvmspec.domain.model.implementation.JvmClassFactoryImpl
import com.seanshubin.jvmspec.domain.model.implementation.JvmFieldFactoryImpl
import com.seanshubin.jvmspec.domain.model.implementation.JvmMethodFactoryImpl
import com.seanshubin.jvmspec.domain.output.formatting.JvmSpecFormat
import com.seanshubin.jvmspec.domain.output.formatting.JvmSpecFormatDetailed
import com.seanshubin.jvmspec.domain.runtime.application.Integrations
import com.seanshubin.jvmspec.inversion.guard.domain.*
import com.seanshubin.jvmspec.rules.CategoryRule
import com.seanshubin.jvmspec.rules.RuleInterpreter
import java.nio.file.Path
import java.time.Clock

class ApplicationDependencies(
    private val integrations: Integrations,
    private val configuration: Configuration
) {
    private val files: FilesContract = integrations.files
    private val clock: Clock = integrations.clock
    private val emit: (Any?) -> Unit = integrations.emit
    private val baseDir: Path = configuration.baseDir
    private val outputDir: Path = configuration.outputDir
    private val includeFile: List<String> = configuration.includeFile
    private val excludeFile: List<String> = configuration.excludeFile
    private val skipDir: List<String> = configuration.skipDir
    private val core: List<String> = configuration.core
    private val boundary: List<String> = configuration.boundary
    private val failOnUnknown: Boolean = configuration.failOnUnknown
    private val categoryRuleSet: Map<String, CategoryRule> = configuration.categoryRuleSet
    private val notifications: Notifications = LineEmittingNotifications(emit)
    private val stats: Stats = StatsImpl()
    private val attributeFactory: JvmAttributeFactory = JvmAttributeFactoryImpl()
    private val methodFactory: JvmMethodFactory = JvmMethodFactoryImpl(attributeFactory)
    private val fieldFactory: JvmFieldFactory = JvmFieldFactoryImpl(attributeFactory)
    private val classFactory: JvmClassFactory = JvmClassFactoryImpl(methodFactory, fieldFactory, attributeFactory)
    private val converter: Converter = Converter(classFactory)
    private val classFileNameFilter: Filter = RegexFilter(
        "class-file-name",
        mapOf(
            "includeFile" to includeFile,
            "excludeFile" to excludeFile
        ),
        stats::consumeMatchedFilterEvent,
        stats::consumeUnmatchedFilterEvent
    )
    private val directoryFilter: Filter = RegexFilter(
        "directory-name",
        mapOf(
            "skipDir" to skipDir
        ),
        stats::consumeMatchedFilterEvent,
        stats::consumeUnmatchedFilterEvent
    )
    private val fileSelectorFileVisitorFactory: FileSelectorFileVisitorFactory = FileSelectorFileVisitorFactoryImpl(
        directoryFilter,
        classFileNameFilter
    )
    private val fileSelector: FileSelector = FileSelectorImpl(baseDir, files, fileSelectorFileVisitorFactory)
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

    private val qualityMetricsSummarizer: QualityMetricsSummarizer = QualityMetricsSummarizerImpl(
        outputDir
    )

    private val qualityMetricsDetailReportGenerator: QualityMetricsDetailReportGenerator =
        QualityMetricsDetailReportGeneratorImpl()

    private val qualityMetricsDetailSummarizer: QualityMetricsDetailSummarizer = QualityMetricsDetailSummarizerImpl(
        outputDir,
        qualityMetricsDetailReportGenerator
    )

    private val htmlReportSummarizer: HtmlReportSummarizer = HtmlReportSummarizerImpl(
        baseDir,
        outputDir,
        qualityMetricsDetailReportGenerator,
        jvmSpecFormat
    )

    private val htmlStatsSummarizer: HtmlStatsSummarizer = HtmlStatsSummarizerImpl(
        outputDir
    )

    private val timer: Timer = Timer(clock)
    val runner: Runnable = Runner(
        files,
        fileSelector,
        classAnalyzer,
        analysisSummarizer,
        statsSummarizer,
        qualityMetricsSummarizer,
        qualityMetricsDetailSummarizer,
        htmlReportSummarizer,
        htmlStatsSummarizer,
        stats,
        classProcessor,
        commandRunner,
        timer,
        notifications::timeTakenMillis,
        converter
    )

    companion object {
        fun fromConfiguration(integrations: Integrations, configuration: Configuration): Runnable {
            return ApplicationDependencies(integrations, configuration).runner
        }
    }
}