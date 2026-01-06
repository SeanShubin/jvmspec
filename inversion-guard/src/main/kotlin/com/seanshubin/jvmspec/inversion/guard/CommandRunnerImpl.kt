package com.seanshubin.jvmspec.inversion.guard

class CommandRunnerImpl(private val environment: Environment) : CommandRunner {
    override fun runCommand(command: Command) {
        command.run(environment)
    }
}