package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.analysis.statistics.Stats

interface StatsSummarizer {
    fun summarize(stats: Stats): List<Command>
}
