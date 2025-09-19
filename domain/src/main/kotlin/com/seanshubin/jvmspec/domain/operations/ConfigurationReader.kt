package com.seanshubin.jvmspec.domain.operations

import com.seanshubin.jvmspec.domain.dynamic.KeyValueStore
import java.nio.file.Path

class ConfigurationReader(
    private val keyValueStore: KeyValueStore,
    private val createFromConfig: (Configuration) -> Runnable
) : Runnable {
    override fun run() {
        val inputDirName = keyValueStore.load(listOf("inputDir")) as String
        val inputDir = Path.of(inputDirName)
        val outputDirName = keyValueStore.load(listOf("outputDir")) as String
        val outputDir = Path.of(outputDirName)
        val includeMethodPatterns = keyValueStore.load(listOf("includeMethod")) as List<String>
        val excludeMethodPatterns = keyValueStore.load(listOf("excludeMethod")) as List<String>
        val includeClassPatterns = keyValueStore.load(listOf("includeClass")) as List<String>
        val excludeClassPatterns = keyValueStore.load(listOf("excludeClass")) as List<String>
        val configuration = Configuration(
            inputDir,
            outputDir,
            includeMethodPatterns,
            excludeMethodPatterns,
            includeClassPatterns,
            excludeClassPatterns
        )
        createFromConfig(configuration).run()
    }
}
