package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.analysis.filtering.RegexFilter
import com.seanshubin.jvmspec.di.contract.FilesContract
import com.seanshubin.jvmspec.infrastructure.command.CommandRunner
import com.seanshubin.jvmspec.infrastructure.command.CommandRunnerImpl
import com.seanshubin.jvmspec.infrastructure.command.Environment
import com.seanshubin.jvmspec.infrastructure.command.EnvironmentImpl
import com.seanshubin.jvmspec.infrastructure.time.Timer
import com.seanshubin.jvmspec.model.api.JvmAttributeFactory
import com.seanshubin.jvmspec.model.api.JvmClassFactory
import com.seanshubin.jvmspec.model.api.JvmFieldFactory
import com.seanshubin.jvmspec.model.api.JvmMethodFactory
import com.seanshubin.jvmspec.model.conversion.Converter
import com.seanshubin.jvmspec.model.implementation.JvmAttributeFactoryImpl
import com.seanshubin.jvmspec.model.implementation.JvmClassFactoryImpl
import com.seanshubin.jvmspec.model.implementation.JvmFieldFactoryImpl
import com.seanshubin.jvmspec.model.implementation.JvmMethodFactoryImpl
import com.seanshubin.jvmspec.output.formatting.JvmSpecFormat
import com.seanshubin.jvmspec.output.formatting.JvmSpecFormatDetailed
import com.seanshubin.jvmspec.output.reporting.DisassembleReport
import com.seanshubin.jvmspec.output.reporting.Report
import com.seanshubin.jvmspec.runtime.application.Integrations
import com.seanshubin.jvmspec.runtime.application.LineEmittingNotifications
import com.seanshubin.jvmspec.runtime.application.Notifications
import com.seanshubin.jvmspec.runtime.application.ReportGenerator
import com.seanshubin.jvmspec.runtime.configuration.Configuration
import java.time.Clock

class DependenciesWithConfiguration(
    private val configuration: Configuration,
    private val integrations: Integrations
) {
    val files: FilesContract = integrations.files
    val emit: (Any?) -> Unit = integrations.emit
    val environment: Environment = EnvironmentImpl(files)
    val commandRunner: CommandRunner = CommandRunnerImpl(environment)
    val notifications: Notifications = LineEmittingNotifications(emit, commandRunner)
    val clock: Clock = integrations.clock
    val format: JvmSpecFormat = JvmSpecFormatDetailed()
    val disassembleReport: Report = DisassembleReport(format)
    val attributeFactory: JvmAttributeFactory = JvmAttributeFactoryImpl()
    val methodFactory: JvmMethodFactory = JvmMethodFactoryImpl(attributeFactory)
    val fieldFactory: JvmFieldFactory = JvmFieldFactoryImpl(attributeFactory)
    val classFactory: JvmClassFactory = JvmClassFactoryImpl(methodFactory, fieldFactory, attributeFactory)
    val converter: Converter = Converter(classFactory)
    val classFileNameFilter: RegexFilter = RegexFilter(
        "class-file-name",
        mapOf(
            "include" to configuration.include,
            "exclude" to configuration.exclude
        ),
        FilterEventHandlers.nopMatchedHandler,
        FilterEventHandlers.nopUnmatchedHandler
    )
    val timer: Timer = Timer(clock)
    val runner: Runnable = ReportGenerator(
        configuration.inputDir,
        configuration.outputDir,
        classFileNameFilter,
        files,
        timer,
        notifications,
        disassembleReport,
        converter
    )
}
