package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.domain.files.FilesContract
import com.seanshubin.jvmspec.domain.files.FilesDelegate
import com.seanshubin.jvmspec.domain.operations.Disassembler
import com.seanshubin.jvmspec.domain.operations.LineEmittingNotifications
import com.seanshubin.jvmspec.domain.operations.Notifications
import java.time.Clock

class Dependencies(args:Array<String>) {
    val files: FilesContract = FilesDelegate
    val emit:(Any?)->Unit = ::println
    val notifications: Notifications = LineEmittingNotifications(emit)
    val clock:Clock = Clock.systemUTC()
    val runner: Runnable = Disassembler(args, files, clock, notifications)
}
