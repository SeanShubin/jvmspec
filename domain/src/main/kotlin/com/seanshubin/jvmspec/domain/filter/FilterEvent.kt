package com.seanshubin.jvmspec.domain.filter

data class FilterEvent(
    val category: String,
    val type: String,
    val pattern: String,
    val text: String
) {
    companion object {
        val consumeNop: ((FilterEvent) -> Unit) = {}
    }
}
