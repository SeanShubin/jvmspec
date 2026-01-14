package com.seanshubin.jvmspec.domain.analysis.filtering

interface Filter {
    fun match(text: String): Set<String>
}
