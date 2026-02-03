package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.runtime.application.Integrations
import com.seanshubin.jvmspec.runtime.application.ProductionIntegrations

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val integrations: Integrations = ProductionIntegrations
        DependenciesWithArgs(args, integrations).runner.run()
    }
}
