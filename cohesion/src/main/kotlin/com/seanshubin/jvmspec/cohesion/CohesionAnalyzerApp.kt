package com.seanshubin.jvmspec.cohesion

object CohesionAnalyzerApp {
    @JvmStatic
    fun main(args: Array<String>) {
        Dependencies(args).runner.run()
    }
}