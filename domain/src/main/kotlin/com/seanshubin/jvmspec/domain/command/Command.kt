package com.seanshubin.jvmspec.domain.command

interface Command {
    fun execute(environment: Environment)
}