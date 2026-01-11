package com.seanshubin.jvmspec.domain.stats

import com.seanshubin.jvmspec.domain.filter.FilterEvent

interface Stats {
    val filterEvents: List<FilterEvent>
    fun consumeFilterEvent(filterEvent: FilterEvent)
}
