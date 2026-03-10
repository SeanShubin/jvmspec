package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.composition.Application
import com.seanshubin.jvmspec.composition.Integrations
import kotlin.system.exitProcess

object EntryPoint {
    @JvmStatic
    fun main(args: Array<String>) {
        val integrations: Integrations = ProductionIntegrations(args)
        val exitCode = Application.execute(integrations)
        exitProcess(exitCode)
    }
}
