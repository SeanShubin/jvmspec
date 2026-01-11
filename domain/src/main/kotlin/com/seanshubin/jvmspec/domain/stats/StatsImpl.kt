package com.seanshubin.jvmspec.domain.stats

import com.seanshubin.jvmspec.domain.filter.MatchedFilterEvent
import com.seanshubin.jvmspec.domain.filter.UnmatchedFilterEvent
import java.util.concurrent.ConcurrentLinkedQueue

class StatsImpl : Stats {
    private val threadSafeMatchedFilterEvents = ConcurrentLinkedQueue<MatchedFilterEvent>()
    private val threadSafeUnmatchedFilterEvents = ConcurrentLinkedQueue<UnmatchedFilterEvent>()

    override val matchedFilterEvents: List<MatchedFilterEvent>
        get() = threadSafeMatchedFilterEvents.toList()

    override val unmatchedFilterEvents: List<UnmatchedFilterEvent>
        get() = threadSafeUnmatchedFilterEvents.toList()

    override fun consumeMatchedFilterEvent(event: MatchedFilterEvent) {
        threadSafeMatchedFilterEvents.add(event)
    }

    override fun consumeUnmatchedFilterEvent(event: UnmatchedFilterEvent) {
        threadSafeUnmatchedFilterEvents.add(event)
    }
}