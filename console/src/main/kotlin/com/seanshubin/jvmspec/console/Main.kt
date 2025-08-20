package com.seanshubin.jvmspec.console

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val dependencies = Dependencies(args)
        dependencies.runner.run()
    }
}
