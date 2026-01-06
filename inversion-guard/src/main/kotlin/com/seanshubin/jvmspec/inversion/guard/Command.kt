package com.seanshubin.jvmspec.inversion.guard

interface Command {
    fun run(environment: Environment)
}