package com.seanshubin.jvmspec.domain.stats

import com.seanshubin.jvmspec.domain.filter.MatchedFilterEvent
import com.seanshubin.jvmspec.domain.filter.UnmatchedFilterEvent

interface Stats {
    val matchedFilterEvents: List<MatchedFilterEvent>
    val unmatchedFilterEvents: List<UnmatchedFilterEvent>
    fun consumeMatchedFilterEvent(event: MatchedFilterEvent)
    fun consumeUnmatchedFilterEvent(event: UnmatchedFilterEvent)
}
