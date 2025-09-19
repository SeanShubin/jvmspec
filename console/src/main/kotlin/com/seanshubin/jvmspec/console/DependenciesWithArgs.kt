package com.seanshubin.jvmspec.console

import com.seanshubin.jvmspec.domain.dynamic.JsonFileKeyValueStore
import com.seanshubin.jvmspec.domain.dynamic.KeyValueStore
import com.seanshubin.jvmspec.domain.files.FilesContract
import com.seanshubin.jvmspec.domain.files.FilesDelegate
import com.seanshubin.jvmspec.domain.operations.Configuration
import com.seanshubin.jvmspec.domain.operations.ConfigurationReader
import java.nio.file.Paths

class DependenciesWithArgs(args: Array<String>) {
    val configFileName = args.getOrNull(0) ?: "config.json"
    val configFilePath = Paths.get(configFileName)
    val files: FilesContract = FilesDelegate
    val keyValueStore: KeyValueStore = JsonFileKeyValueStore(configFilePath, files)
    val createRunner: (Configuration) -> Runnable = { configuration ->
        DependenciesWithConfiguration(configuration).runner
    }
    val runner: Runnable = ConfigurationReader(keyValueStore, createRunner)
}
