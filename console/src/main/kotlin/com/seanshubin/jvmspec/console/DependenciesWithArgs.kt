package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.contract.FilesDelegate
import com.seanshubin.jvmspec.domain.runtime.configuration.Configuration
import com.seanshubin.jvmspec.domain.runtime.configuration.ConfigurationReader
import com.seanshubin.jvmspec.domain.runtime.storage.JsonFileKeyValueStore
import com.seanshubin.jvmspec.domain.runtime.storage.KeyValueStore
import java.nio.file.Paths

class DependenciesWithArgs(args: Array<String>) {
    val configFileName = args.getOrNull(0) ?: "config.json"
    val configFilePath = Paths.get(configFileName)
    val files: FilesContract = FilesDelegate.defaultInstance()
    val keyValueStore: KeyValueStore = JsonFileKeyValueStore(configFilePath, files)
    val createRunner: (Configuration) -> Runnable = { configuration ->
        DependenciesWithConfiguration(configuration).runner
    }
    val runner: Runnable = ConfigurationReader(keyValueStore, createRunner)
}
