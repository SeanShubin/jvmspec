package com.seanshubin.jvmspec.domain.util

object RegexUtil {
    fun createMatchFunctionFromList(
        whitelistPatterns: List<String>,
        blacklistPatterns: List<String>
    ): (String) -> FilterResult = { s ->
        val isOnWhitelist = whitelistPatterns.map { Regex(it) }.any { it.matches(s) }
        val isOnBlacklist = blacklistPatterns.map { Regex(it) }.any { it.matches(s) }
        when {
            isOnWhitelist && isOnBlacklist -> FilterResult.BOTH_WHITELIST_AND_BLACKLIST
            isOnWhitelist && !isOnBlacklist -> FilterResult.WHITELIST_ONLY
            !isOnWhitelist && isOnBlacklist -> FilterResult.BLACKLIST_ONLY
            else -> FilterResult.UNKNOWN
        }
    }
}
