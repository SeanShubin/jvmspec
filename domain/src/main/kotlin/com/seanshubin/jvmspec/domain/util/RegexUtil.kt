package com.seanshubin.jvmspec.domain.util

object RegexUtil {
    fun createMatchFunctionFromList(
        includePatternList: List<String>,
        excludePatternList: List<String>
    ): (String) -> MatchEnum = { s ->
        val isIncluded = includePatternList.map { Regex(it) }.any { it.matches(s) }
        val isExcluded = excludePatternList.map { Regex(it) }.any { it.matches(s) }
        when {
            isIncluded && isExcluded -> MatchEnum.BOTH_INCLUDED_AND_EXCLUDED
            isIncluded && !isExcluded -> MatchEnum.INCLUDED
            !isIncluded && isExcluded -> MatchEnum.EXCLUDED
            else -> MatchEnum.NEITHER_INCLUDED_NOR_EXCLUDED
        }
    }
}
