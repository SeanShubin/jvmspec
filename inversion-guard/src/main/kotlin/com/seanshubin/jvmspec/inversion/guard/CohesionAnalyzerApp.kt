package com.seanshubin.jvmspec.inversion.guard

object CohesionAnalyzerApp {
    @JvmStatic
    fun main(args: Array<String>) {
        Dependencies(args).runner.run()
    }
}