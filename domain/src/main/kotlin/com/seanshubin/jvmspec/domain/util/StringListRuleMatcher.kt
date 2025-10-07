package com.seanshubin.jvmspec.domain.util

class StringListRuleMatcher(val list: List<String>, var index: Int = 0, var rulesMatched: Boolean = true) {
    fun expectZeroOrMore(rule: (String) -> Boolean) {
        if (!rulesMatched) return
        while (!atEnd() && rule(current())) {
            index++
        }
    }

    fun expectExactly(quantity: Int, rule: (String) -> Boolean) {
        if (!rulesMatched) return
        if (quantity != 0 && atEnd()) rulesMatched = false
        val targetIndex = index + quantity
        while (rulesMatched && !atEnd() && index < targetIndex) {
            if (!rule(current())) {
                rulesMatched = false
            }
            index++
        }
    }

    fun expectAtEnd() {
        if (!atEnd()) rulesMatched = false
    }

    private fun atEnd() = index == list.size
    private fun current(): String = list[index]
}
