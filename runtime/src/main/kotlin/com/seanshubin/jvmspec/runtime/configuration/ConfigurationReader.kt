package com.seanshubin.jvmspec.runtime.configuration

import com.seanshubin.jvmspec.infrastructure.types.TypeSafety.toTypedList
import com.seanshubin.jvmspec.runtime.storage.KeyValueStore
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
        val include = keyValueStore.load(listOf("includeFile")).toTypedList<String>()
        val exclude = keyValueStore.load(listOf("excludeFile")).toTypedList<String>()
        val configuration = Configuration(
            inputDir,
            outputDir,
            include,
            exclude
        )
        createFromConfig(configuration).run()
    }
}
