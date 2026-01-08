package com.seanshubin.jvmspec.inversion.guard

class RegexMatcher(private val includePatterns: List<String>, private val excludePatterns: List<String>) {
    val includeRegexList = includePatterns.map { Regex(it) }
    val excludeRegexList = excludePatterns.map { Regex(it) }
    fun match(line: String): MatchResult {
        val matchesInclude = includeRegexList.any { it.matches(line) }
        val matchesExclude = excludeRegexList.any { it.matches(line) }
        return when {
            matchesInclude && matchesExclude -> MatchResult.BOTH
            matchesInclude -> MatchResult.INCLUDE_ONLY
            matchesExclude -> MatchResult.EXCLUDE_ONLY
            else -> MatchResult.NEITHER
        }
    }

    enum class MatchResult {
        NEITHER,
        INCLUDE_ONLY,
        EXCLUDE_ONLY,
        BOTH
    }
}