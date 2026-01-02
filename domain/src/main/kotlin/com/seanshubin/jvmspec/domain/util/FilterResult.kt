package com.seanshubin.jvmspec.domain.util

enum class FilterResult {
    UNKNOWN,
    WHITELIST_ONLY,
    BLACKLIST_ONLY,
    BOTH_WHITELIST_AND_BLACKLIST,
}
