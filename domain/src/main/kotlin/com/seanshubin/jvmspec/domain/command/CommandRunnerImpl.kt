package com.seanshubin.jvmspec.domain.command

class CommandRunnerImpl(val environment: Environment) : CommandRunner {
    override fun run(command: Command) {
        command.execute(environment)
    }
}