package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.configuration.FixedPathJsonFileKeyValueStoreFactory
import com.seanshubin.jvmspec.domain.infrastructure.types.TypeSafety.toTypedList
import com.seanshubin.jvmspec.domain.runtime.application.Integrations
import com.seanshubin.jvmspec.rules.RuleLoader
import java.nio.file.Path

class ConfiguredRunnerFactory(
    private val args: Array<String>,
    private val createRunner: (Integrations, Configuration) -> Runnable,
    private val keyValueStoreFactory: FixedPathJsonFileKeyValueStoreFactory,
    private val ruleLoader: RuleLoader,
    private val integrations: Integrations,
) {
    fun createConfiguredRunner(): Runnable {
        val configPathName = args.getOrNull(0) ?: "inversion-guard-config.json"
        val configPath = Path.of(configPathName)
        val keyValueStore = keyValueStoreFactory.create(configPath)
        val baseDirName = keyValueStore.loadOrCreateDefault(listOf("inputDir"), ".") as String
        val outputDirName =
            keyValueStore.loadOrCreateDefault(listOf("outputDir"), "generated/inversion-guard-report") as String
        val includeFile =
            keyValueStore.loadOrCreateDefault(
                listOf("classes", "includeFile"),
                listOf(".*/target/.*\\.class")
            ).toTypedList<String>()
        val excludeFile =
            keyValueStore.loadOrCreateDefault(listOf("classes", "excludeFile"), emptyList<String>())
                .toTypedList<String>()
        val skipDir =
            keyValueStore.loadOrCreateDefault(listOf("classes", "skipDir"), emptyList<String>()).toTypedList<String>()
        val rulesFileName =
            keyValueStore.loadOrCreateDefault(listOf("globalRules"), "inversion-guard-rules.json") as String
        val localCore =
            keyValueStore.loadOrCreateDefault(listOf("localRules", "core"), emptyList<String>()).toTypedList<String>()
        val localBoundary =
            keyValueStore.loadOrCreateDefault(listOf("localRules", "boundary"), emptyList<String>())
                .toTypedList<String>()
        val failOnUnknown = keyValueStore.loadOrCreateDefault(listOf("failOnUnknown"), false) as Boolean
        val baseDir = Path.of(baseDirName)
        val outputDir = Path.of(outputDirName)
        val rulesFile = Path.of(rulesFileName)
        val rulesJson = integrations.files.readString(rulesFile)
        val rulesData = ruleLoader.load(rulesJson)
        val categories = rulesData.categories
        val globalCore = rulesData.core
        val globalBoundary = rulesData.boundary
        val core = localCore + globalCore
        val boundary = localBoundary + globalBoundary
        val configuration = Configuration(
            baseDir,
            outputDir,
            includeFile,
            excludeFile,
            skipDir,
            core,
            boundary,
            failOnUnknown,
            categories
        )
        return createRunner(integrations, configuration)
    }
}
