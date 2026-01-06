package com.seanshubin.jvmspec.rules

data class CategoryRule(
    val methodConstraints: MethodConstraints?,
    val rules: List<RuleQuantifier>
)
