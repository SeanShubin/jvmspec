package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.domain.command.CommandRunner
import com.seanshubin.jvmspec.domain.command.CommandRunnerImpl
import com.seanshubin.jvmspec.domain.command.Environment
import com.seanshubin.jvmspec.domain.command.EnvironmentImpl
import com.seanshubin.jvmspec.domain.files.FilesContract
import com.seanshubin.jvmspec.domain.files.FilesDelegate
import com.seanshubin.jvmspec.domain.operations.*
import java.time.Clock

class Dependencies(args: Array<String>) {
    val files: FilesContract = FilesDelegate
    val emit: (Any?) -> Unit = ::println
    val environment: Environment = EnvironmentImpl(files)
    val commandRunner: CommandRunner = CommandRunnerImpl(environment)
    val notifications: Notifications = LineEmittingNotifications(emit, commandRunner)
    val clock: Clock = Clock.systemUTC()
    val disassembleReport: Report = DisassembleReport()
    val methodReport: Report = MethodReport()
    val compositeReport: Report = CompositeReport(
        listOf(
            disassembleReport,
            methodReport
        )
    )
    val runner: Runnable = ReportGenerator(args, files, clock, notifications, compositeReport)
}
