package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.configuration.FixedPathJsonFileKeyValueStoreFactory
import com.seanshubin.jvmspec.configuration.FixedPathJsonFileKeyValueStoreFactoryImpl
import com.seanshubin.jvmspec.contract.FilesContract
import com.seanshubin.jvmspec.contract.FilesDelegate
import com.seanshubin.jvmspec.rules.JsonRuleLoader
import com.seanshubin.jvmspec.rules.RuleLoader

class BootstrapDependencies(
    private val args: Array<String>
) {
    private val createRunner: (configuration: Configuration) -> Runnable = { configuration ->
        ApplicationDependencies(
            configuration.baseDir,
            configuration.outputDir,
            configuration.include,
            configuration.exclude,
            configuration.core,
            configuration.boundary,
            configuration.categoryRuleSet
        ).runner
    }

    val files: FilesContract = FilesDelegate
    val keyValueStoreFactory: FixedPathJsonFileKeyValueStoreFactory = FixedPathJsonFileKeyValueStoreFactoryImpl(files)
    val ruleLoader: RuleLoader = JsonRuleLoader()
    val runner: Runnable = ConfigurationRunner(
        args,
        createRunner,
        keyValueStoreFactory,
        ruleLoader,
        files
    )
}
