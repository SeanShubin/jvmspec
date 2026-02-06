package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.runtime.application.Integrations
import com.seanshubin.jvmspec.runtime.application.ProductionIntegrations

object EntryPoint {
    @JvmStatic
    fun main(args: Array<String>) {
        val integrations: Integrations = ProductionIntegrations
        ArgsDependencies(args, integrations).runner.run()
    }
}
