package com.seanshubin.jvmspec.domain.analysis.statistics

import com.seanshubin.jvmspec.domain.analysis.filtering.MatchedFilterEvent
import com.seanshubin.jvmspec.domain.analysis.filtering.UnmatchedFilterEvent

interface Stats {
    val matchedFilterEvents: List<MatchedFilterEvent>
    val unmatchedFilterEvents: List<UnmatchedFilterEvent>
    fun consumeMatchedFilterEvent(event: MatchedFilterEvent)
    fun consumeUnmatchedFilterEvent(event: UnmatchedFilterEvent)
}
