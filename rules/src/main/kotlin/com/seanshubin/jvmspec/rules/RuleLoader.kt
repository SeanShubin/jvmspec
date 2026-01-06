package com.seanshubin.jvmspec.rules

interface RuleLoader {
    fun load(json: String): CategoryRuleSet
}
