package com.seanshubin.jvmspec.inversion.guard

object InversionGuardApp {
    @JvmStatic
    fun main(args: Array<String>) {
        Dependencies(args).runner.run()
    }
}