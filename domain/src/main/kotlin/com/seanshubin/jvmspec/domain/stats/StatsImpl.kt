package com.seanshubin.jvmspec.domain.stats

import com.seanshubin.jvmspec.domain.filter.FilterEvent
import java.util.concurrent.ConcurrentLinkedQueue

class StatsImpl : Stats {
    private val threadSafeFilterEvents = ConcurrentLinkedQueue<FilterEvent>()
    override val filterEvents: List<FilterEvent>
        get() = threadSafeFilterEvents.toList()

    override fun consumeFilterEvent(filterEvent: FilterEvent) {
        threadSafeFilterEvents.add(filterEvent)
    }
}