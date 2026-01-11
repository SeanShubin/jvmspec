package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.stats.Stats

interface StatsSummarizer {
    fun summarize(stats: Stats): List<Command>
}
