package com.seanshubin.jvmspec.domain.infrastructure.command

interface Command {
    fun execute(environment: Environment)
}