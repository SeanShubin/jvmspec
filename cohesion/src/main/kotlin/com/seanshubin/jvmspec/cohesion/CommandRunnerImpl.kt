package com.seanshubin.jvmspec.cohesion

class CommandRunnerImpl(private val environment: Environment) : CommandRunner {
    override fun runCommand(command: Command) {
        command.run(environment)
    }
}