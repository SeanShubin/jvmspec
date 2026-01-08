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
        val include = keyValueStore.load(listOf("include")) as List<String>
        val exclude = keyValueStore.load(listOf("exclude")) as List<String>
        val configuration = Configuration(
            inputDir,
            outputDir,
            include,
            exclude
        )
        createFromConfig(configuration).run()
    }
}
