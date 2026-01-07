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
    val files: FilesContract = FilesDelegate
    val keyValueStoreFactory: FixedPathJsonFileKeyValueStoreFactory = FixedPathJsonFileKeyValueStoreFactoryImpl(files)
    val ruleLoader: RuleLoader = JsonRuleLoader()
    val configuredRunnerFactory: ConfiguredRunnerFactory = ConfiguredRunnerFactory(
        args,
        ApplicationDependencies::fromConfiguration,
        keyValueStoreFactory,
        ruleLoader,
        files
    )
    val runner: Runnable = configuredRunnerFactory.createConfiguredRunner()
}
