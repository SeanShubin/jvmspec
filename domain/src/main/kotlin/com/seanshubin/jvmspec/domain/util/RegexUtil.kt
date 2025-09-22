package com.seanshubin.jvmspec.domain.util

object RegexUtil {
    fun createMatchFunctionFromList(
        whitelistPatterns: List<String>,
        blacklistPatterns: List<String>
    ): (String) -> MatchEnum = { s ->
        val isOnWhitelist = whitelistPatterns.map { Regex(it) }.any { it.matches(s) }
        val isOnBlacklist = blacklistPatterns.map { Regex(it) }.any { it.matches(s) }
        when {
            isOnWhitelist && isOnBlacklist -> MatchEnum.BOTH_WHITELIST_AND_BLACKLIST
            isOnWhitelist && !isOnBlacklist -> MatchEnum.WHITELIST_ONLY
            !isOnWhitelist && isOnBlacklist -> MatchEnum.BLACKLIST_ONLY
            else -> MatchEnum.UNKNOWN
        }
    }
}
