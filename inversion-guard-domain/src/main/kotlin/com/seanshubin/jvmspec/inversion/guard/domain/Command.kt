package com.seanshubin.jvmspec.inversion.guard.domain

interface Command {
    fun run(environment: Environment)
}