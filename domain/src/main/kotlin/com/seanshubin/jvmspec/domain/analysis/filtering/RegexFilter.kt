package com.seanshubin.jvmspec.domain.analysis.filtering

class RegexFilter(
    private val category: String,
    private val patternsByType: Map<String, List<String>>,
    private val matchedFilterEvent: (MatchedFilterEvent) -> Unit,
    private val unmatchedFilterEvent: (UnmatchedFilterEvent) -> Unit
) : Filter {
    private val regexesByType: Map<String, List<Regex>> =
        patternsByType.mapValues { (_, patterns) ->
            patterns.map { Regex(it) }
        }

    override fun match(text: String): Set<String> {
        val matchedTypes = mutableSetOf<String>()
        var hasMatch = false

        regexesByType.forEach { (type, regexList) ->
            regexList.forEach { regex ->
                if (regex.matches(text)) {
                    matchedFilterEvent(MatchedFilterEvent(category, type, regex.pattern, text))
                    matchedTypes.add(type)
                    hasMatch = true
                }
            }
        }

        if (!hasMatch) {
            unmatchedFilterEvent(UnmatchedFilterEvent(category, text))
        }

        return matchedTypes
    }
}
