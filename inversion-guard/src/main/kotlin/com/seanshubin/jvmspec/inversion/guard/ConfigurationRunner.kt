package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.configuration.FixedPathJsonFileKeyValueStoreFactory
import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.rules.RuleLoader
import java.nio.file.Path

class ConfigurationRunner(
    private val args: Array<String>,
    private val createRunner: (Configuration) -> Runnable,
    private val keyValueStoreFactory: FixedPathJsonFileKeyValueStoreFactory,
    private val ruleLoader: RuleLoader,
    private val files: FilesContract,
) : Runnable {
    override fun run() {
        val configPathName = args.getOrNull(0) ?: "inversion-guard-config.json"
        val configPath = Path.of(configPathName)
        val keyValueStore = keyValueStoreFactory.create(configPath)
        val baseDirName = keyValueStore.loadOrCreateDefault(listOf("inputDir"), ".") as String
        val outputDirName =
            keyValueStore.loadOrCreateDefault(listOf("outputDir"), "generated/inversion-guard-report") as String
        val includeClasses =
            keyValueStore.loadOrCreateDefault(
                listOf("classes", "include"),
                listOf(".*/target/.*\\.class")
            ) as List<String>
        val excludeClasses =
            keyValueStore.loadOrCreateDefault(listOf("classes", "exclude"), emptyList<String>()) as List<String>
        val rulesFileName = keyValueStore.loadOrCreateDefault(listOf("rules"), "inversion-guard-rules.json") as String
        val baseDir = Path.of(baseDirName)
        val outputDir = Path.of(outputDirName)
        val rulesFile = Path.of(rulesFileName)
        val rulesJson = files.readString(rulesFile)
        val rulesData = ruleLoader.load(rulesJson)
        val categories = rulesData.categories
        val core = rulesData.core
        val boundary = rulesData.boundary
        val configuration = Configuration(
            baseDir,
            outputDir,
            includeClasses,
            excludeClasses,
            core,
            boundary,
            categories
        )
        createRunner(configuration).run()
    }
}
