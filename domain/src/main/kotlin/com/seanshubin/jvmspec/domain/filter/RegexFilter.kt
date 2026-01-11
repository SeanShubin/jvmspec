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

        // Map: pattern -> Set of types that matched this pattern
        val patternToTypes = mutableMapOf<String, MutableSet<String>>()

        // First pass: collect all matches
        regexesByType.forEach { (type, regexList) ->
            regexList.forEach { regex ->
                if (regex.matches(text)) {
                    patternToTypes
                        .getOrPut(regex.pattern) { mutableSetOf() }
                        .add(type)
                    matchedTypes.add(type)
                }
            }
        }

        // Second pass: emit one event per (pattern, text) with all matched types
        patternToTypes.forEach { (pattern, types) ->
            filterEvent(FilterEvent(category, types.toSet(), pattern, text))
        }

        return matchedTypes
    }

}