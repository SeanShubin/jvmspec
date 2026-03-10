package com.seanshubin.jvmspec.composition

object Application {
    fun run(integrations: Integrations) {
        val classFilePathString = integrations.commandLineArgs.getOrNull(0)
            ?: throw IllegalArgumentException("Usage: java -jar jvmspec.jar <class-file-path>")

        val applicationDeps = ApplicationDependencies(integrations, classFilePathString)
        applicationDeps.runner.run()
    }

    // Handles exceptions and returns exit code
    fun execute(integrations: Integrations): Int {
        return try {
            run(integrations)
            0 // SUCCESS
        } catch (e: Exception) {
            integrations.systemErr.println("Error: ${e.message}")
            e.printStackTrace(integrations.systemErr)
            1 // GENERAL_ERROR
        }
    }
}
