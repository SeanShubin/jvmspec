package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.contract.FilesDelegate
import com.seanshubin.jvmspec.domain.command.CommandRunner
import com.seanshubin.jvmspec.domain.command.CommandRunnerImpl
import com.seanshubin.jvmspec.domain.command.Environment
import com.seanshubin.jvmspec.domain.command.EnvironmentImpl
import com.seanshubin.jvmspec.domain.filter.FilterEvent
import com.seanshubin.jvmspec.domain.filter.RegexFilter
import com.seanshubin.jvmspec.domain.format.JvmSpecFormat
import com.seanshubin.jvmspec.domain.format.JvmSpecFormatDetailed
import com.seanshubin.jvmspec.domain.operations.*
import com.seanshubin.jvmspec.domain.util.Timer
import java.time.Clock

class DependenciesWithConfiguration(private val configuration: Configuration) {
    val files: FilesContract = FilesDelegate.defaultInstance()
    val emit: (Any?) -> Unit = ::println
    val environment: Environment = EnvironmentImpl(files)
    val commandRunner: CommandRunner = CommandRunnerImpl(environment)
    val notifications: Notifications = LineEmittingNotifications(emit, commandRunner)
    val clock: Clock = Clock.systemUTC()
    val format: JvmSpecFormat = JvmSpecFormatDetailed()
    val disassembleReport: Report = DisassembleReport(format)
    val classFileNameFilter: RegexFilter = RegexFilter(
        configuration.include,
        configuration.exclude,
        "class-file-name",
        FilterEvent.consumeNop
    )
    val timer: Timer = Timer(clock)
    val runner: Runnable = ReportGenerator(
        configuration.inputDir,
        configuration.outputDir,
        classFileNameFilter,
        files,
        timer,
        notifications,
        disassembleReport
    )
}
