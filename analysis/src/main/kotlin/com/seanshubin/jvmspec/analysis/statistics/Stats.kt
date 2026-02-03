package com.seanshubin.jvmspec.analysis.statistics

import com.seanshubin.jvmspec.analysis.filtering.MatchedFilterEvent
import com.seanshubin.jvmspec.analysis.filtering.UnmatchedFilterEvent

interface Stats {
    val matchedFilterEvents: List<MatchedFilterEvent>
    val unmatchedFilterEvents: List<UnmatchedFilterEvent>
    val registeredPatterns: Map<String, Map<String, List<String>>> // category -> type -> patterns
    val registeredLocalPatterns: Map<String, Map<String, List<String>>> // category -> type -> local patterns
    fun consumeMatchedFilterEvent(event: MatchedFilterEvent)
    fun consumeUnmatchedFilterEvent(event: UnmatchedFilterEvent)
    fun registerPatterns(category: String, patternsByType: Map<String, List<String>>)
    fun registerLocalPatterns(category: String, patternsByType: Map<String, List<String>>)
}
