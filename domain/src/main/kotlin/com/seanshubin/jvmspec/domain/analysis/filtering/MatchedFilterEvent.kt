package com.seanshubin.jvmspec.domain.analysis.filtering

data class MatchedFilterEvent(
    val category: String,
    val type: String,
    val pattern: String,
    val text: String
)
