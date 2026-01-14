package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.analysis.statistics.Stats

interface StatsSummarizer {
    fun summarize(stats: Stats): List<Command>
}
