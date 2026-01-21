package com.seanshubin.jvmspec.inversion.guard.app

import com.seanshubin.jvmspec.domain.runtime.application.Integrations
import com.seanshubin.jvmspec.domain.runtime.application.ProductionIntegrations

object InversionGuardApp {
    @JvmStatic
    fun main(args: Array<String>) {
        val integrations: Integrations = ProductionIntegrations
        BootstrapDependencies(args, integrations).runner.run()
    }
}