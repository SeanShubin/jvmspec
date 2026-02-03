package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.runtime.application.Integrations
import com.seanshubin.jvmspec.runtime.configuration.Configuration
import com.seanshubin.jvmspec.runtime.configuration.ConfigurationReader
import com.seanshubin.jvmspec.runtime.storage.JsonFileKeyValueStore
import com.seanshubin.jvmspec.runtime.storage.KeyValueStore
import java.nio.file.Paths

class DependenciesWithArgs(
    args: Array<String>,
    private val integrations: Integrations
) {
    val configFileName = ConfigFileNameResolver.resolveConfigFileName(args)
    val configFilePath = Paths.get(configFileName)
    val keyValueStore: KeyValueStore = JsonFileKeyValueStore(configFilePath, integrations.files)
    val createRunner: (Configuration) -> Runnable = { configuration ->
        DependenciesWithConfiguration(configuration, integrations).runner
    }
    val runner: Runnable = ConfigurationReader(keyValueStore, createRunner)
}
