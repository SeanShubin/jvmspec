package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.composition.Integrations
import com.seanshubin.jvmspec.di.contract.FilesContract
import com.seanshubin.jvmspec.di.delegate.FilesDelegate
import java.io.PrintStream
import java.time.Clock

class ProductionIntegrations(
    override val commandLineArgs: Array<String>
) : Integrations {
    override val files: FilesContract = FilesDelegate.defaultInstance()
    override val clock: Clock = Clock.systemUTC()
    override val emit: (Any?) -> Unit = ::println
    override val systemOut: PrintStream = System.out
    override val systemErr: PrintStream = System.err
}
