package com.seanshubin.jvmspec.domain.filter

data class UnmatchedFilterEvent(
    val category: String,
    val text: String
)
