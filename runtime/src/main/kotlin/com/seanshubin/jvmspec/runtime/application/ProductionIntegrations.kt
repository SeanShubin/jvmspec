package com.seanshubin.jvmspec.runtime.application

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.contract.FilesDelegate
import java.time.Clock

object ProductionIntegrations : Integrations {
    override val files: FilesContract = FilesDelegate.defaultInstance()
    override val clock: Clock = Clock.systemUTC()
    override val emit: (Any?) -> Unit = ::println
}
