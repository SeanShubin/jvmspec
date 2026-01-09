package com.seanshubin.jvmspec.domain.filter

interface Filter {
    fun match(text: String): FilterResult
}
