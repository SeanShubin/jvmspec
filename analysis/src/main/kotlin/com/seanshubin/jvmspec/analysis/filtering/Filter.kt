package com.seanshubin.jvmspec.analysis.filtering

interface Filter {
    fun match(text: String): Set<String>
}
