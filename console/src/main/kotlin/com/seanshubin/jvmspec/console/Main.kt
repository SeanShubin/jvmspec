package com.seanshubin.jvmspec.console

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        DependenciesWithArgs(args).runner.run()
    }
}
