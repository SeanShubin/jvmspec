package com.seanshubin.jvmspec.inversion.guard.app

import com.seanshubin.jvmspec.configuration.FixedPathJsonFileKeyValueStoreFactory
import com.seanshubin.jvmspec.configuration.FixedPathJsonFileKeyValueStoreFactoryImpl
import com.seanshubin.jvmspec.domain.runtime.application.Integrations
import com.seanshubin.jvmspec.inversion.guard.domain.ConfiguredRunnerFactory
import com.seanshubin.jvmspec.rules.JsonRuleLoader
import com.seanshubin.jvmspec.rules.RuleLoader

class BootstrapDependencies(
    private val args: Array<String>,
    private val integrations: Integrations
) {
    val keyValueStoreFactory: FixedPathJsonFileKeyValueStoreFactory =
        FixedPathJsonFileKeyValueStoreFactoryImpl(integrations.files)
    val ruleLoader: RuleLoader = JsonRuleLoader()
    val configuredRunnerFactory: ConfiguredRunnerFactory = ConfiguredRunnerFactory(
        args,
        ApplicationDependencies::fromConfiguration,
        keyValueStoreFactory,
        ruleLoader,
        integrations
    )
    val runner: Runnable = configuredRunnerFactory.createConfiguredRunner()
}
