package com.seanshubin.jvmspec.domain.filter

class RegexFilter(
    private val includePatterns: List<String>,
    private val excludePatterns: List<String>
) : Filter {
    val includeRegexList = includePatterns.map { Regex(it) }
    val excludeRegexList = excludePatterns.map { Regex(it) }
    override fun match(text: String): FilterResult {
        val matchesInclude = includeRegexList.any { it.matches(text) }
        val matchesExclude = excludeRegexList.any { it.matches(text) }
        return when {
            matchesInclude && matchesExclude -> FilterResult.BOTH
            matchesInclude -> FilterResult.INCLUDE_ONLY
            matchesExclude -> FilterResult.EXCLUDE_ONLY
            else -> FilterResult.NEITHER
        }
    }

}