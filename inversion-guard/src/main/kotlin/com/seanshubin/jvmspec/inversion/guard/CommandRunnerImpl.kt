package com.seanshubin.jvmspec.inversion.guard

class CommandRunnerImpl(
    private val environment: Environment,
    private val runCommandEvent: (Command) -> Unit
) : CommandRunner {
    override fun runCommand(command: Command) {
        runCommandEvent(command)
        command.run(environment)
    }
}