package com.seanshubin.jvmspec.cohesion

interface Command {
    fun run(environment: Environment)
}