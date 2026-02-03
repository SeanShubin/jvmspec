package com.seanshubin.jvmspec.infrastructure.command

interface Command {
    fun execute(environment: Environment)
}