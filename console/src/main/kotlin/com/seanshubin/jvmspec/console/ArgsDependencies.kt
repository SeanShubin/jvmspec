package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.runtime.application.Dependencies
import com.seanshubin.jvmspec.runtime.application.Integrations

class ArgsDependencies(
    args: Array<String>,
    integrations: Integrations
) {
    private val classFilePath = args.getOrNull(0)
        ?: throw IllegalArgumentException("Usage: java -jar jvmspec.jar <class-file-path>")
    private val dependencies = Dependencies(integrations, classFilePath)
    val runner: Runnable = dependencies.runner
}
