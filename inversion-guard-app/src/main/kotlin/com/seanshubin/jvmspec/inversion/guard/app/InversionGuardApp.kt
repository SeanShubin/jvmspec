package com.seanshubin.jvmspec.inversion.guard.app

object InversionGuardApp {
    @JvmStatic
    fun main(args: Array<String>) {
        BootstrapDependencies(args).runner.run()
    }
}