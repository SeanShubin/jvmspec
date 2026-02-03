package com.seanshubin.jvmspec.infrastructure.command

class CommandRunnerImpl(val environment: Environment) : CommandRunner {
    override fun run(command: Command) {
        command.execute(environment)
    }
}