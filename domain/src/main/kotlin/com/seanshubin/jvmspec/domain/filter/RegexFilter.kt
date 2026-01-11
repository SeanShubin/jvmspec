package com.seanshubin.jvmspec.domain.filter

class RegexFilter(
    private val category: String,
    private val patternsByType: Map<String, List<String>>,
    private val filterEvent: (FilterEvent) -> Unit
) : Filter {
    private val regexesByType: Map<String, List<Regex>> =
        patternsByType.mapValues { (_, patterns) ->
            patterns.map { Regex(it) }
        }

    override fun match(text: String): Set<String> {
        val matchedTypes = mutableSetOf<String>()

        regexesByType.forEach { (type, regexList) ->
            regexList.forEach { regex ->
                if (regex.matches(text)) {
                    filterEvent(FilterEvent(category, type, regex.pattern, text))
                }
            }
            if (regexList.any { it.matches(text) }) {
                matchedTypes.add(type)
            }
        }

        return matchedTypes
    }

}