package com.seanshubin.jvmspec.rules

data class CategoryRuleSet(
    val categories: Map<String, CategoryRule>,
    val core: List<String> = emptyList(),
    val boundary: List<String> = emptyList()
)
