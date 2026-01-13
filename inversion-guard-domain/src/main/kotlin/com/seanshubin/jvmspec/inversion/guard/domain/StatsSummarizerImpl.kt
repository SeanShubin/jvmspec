package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.filter.MatchedFilterEvent
import com.seanshubin.jvmspec.domain.filter.UnmatchedFilterEvent
import com.seanshubin.jvmspec.domain.stats.Stats
import com.seanshubin.jvmspec.domain.tree.Tree
import java.nio.file.Path

class StatsSummarizerImpl(
    private val outputDir: Path
) : StatsSummarizer {
    override fun summarize(stats: Stats): List<Command> {
        // Group matched events by category
        val matchedByCategory = stats.matchedFilterEvents.groupBy { it.category }
        // Group unmatched events by category
        val unmatchedByCategory = stats.unmatchedFilterEvents.groupBy { it.category }

        // Get all categories (from both matched and unmatched)
        val allCategories = (matchedByCategory.keys + unmatchedByCategory.keys).distinct().sorted()

        // Create one file per category
        val commands = allCategories.map { category ->
            val matchedEvents = matchedByCategory[category] ?: emptyList()
            val unmatchedEvents = unmatchedByCategory[category] ?: emptyList()
            val categoryTree = buildCategoryReport(category, matchedEvents, unmatchedEvents)
            val path = outputDir.resolve("stats-$category.txt")
            CreateFileCommand(path, listOf(categoryTree))
        }

        return commands
    }

    private fun buildCategoryReport(
        category: String,
        matchedEvents: List<MatchedFilterEvent>,
        unmatchedEvents: List<UnmatchedFilterEvent>
    ): Tree {
        val multiTypeMatchesSection = buildMultiTypeMatchesSection(matchedEvents)
        val multiPatternMatchesSection = buildMultiPatternMatchesSection(matchedEvents)
        val byTextSection = buildByTextSection(matchedEvents)
        val byPatternSection = buildByPatternSection(matchedEvents)
        val noMatchesSection = buildNoMatchesSection(unmatchedEvents)

        val totalMatched = matchedEvents.size
        val totalUnmatched = unmatchedEvents.size
        val total = totalMatched + totalUnmatched

        return Tree(
            "$category ($total total: $totalMatched matched, $totalUnmatched unmatched)",
            listOf(
                multiTypeMatchesSection,
                multiPatternMatchesSection,
                byTextSection,
                byPatternSection,
                noMatchesSection
            )
        )
    }

    // Section 1: multi-type-matches
    // Grouped by types set, then list texts
    private fun buildMultiTypeMatchesSection(events: List<MatchedFilterEvent>): Tree {
        val eventsByText = events.groupBy { it.text }

        // Filter to only texts with multiple types and group by types set
        val multiTypeTextsByTypes = eventsByText.entries
            .mapNotNull { (text, eventsForText) ->
                val uniqueTypes = eventsForText.map { it.type }.distinct().sorted()
                if (uniqueTypes.size > 1) {
                    text to uniqueTypes
                } else {
                    null
                }
            }
            .groupBy({ it.second }, { it.first })

        val typesSetTrees = multiTypeTextsByTypes.entries
            .sortedBy { it.key.joinToString(", ") }
            .map { (types, texts) ->
                val typesLabel = types.joinToString(", ")
                val textChildren = texts.sorted().map { text ->
                    Tree(text)
                }
                Tree("{$typesLabel} (${texts.size} total)", textChildren)
            }

        val totalMultiTypeTexts = multiTypeTextsByTypes.values.sumOf { it.size }
        return Tree("multi-type-matches ($totalMultiTypeTexts total)", typesSetTrees)
    }

    // Section 2: multi-pattern-matches
    // Grouped by patterns set, then list texts
    private fun buildMultiPatternMatchesSection(events: List<MatchedFilterEvent>): Tree {
        val eventsByText = events.groupBy { it.text }

        // Filter to only texts with multiple patterns and group by patterns set
        val multiPatternTextsByPatterns = eventsByText.entries
            .mapNotNull { (text, eventsForText) ->
                val uniquePatterns = eventsForText.map { it.pattern }.distinct().sorted()
                if (uniquePatterns.size > 1) {
                    text to uniquePatterns
                } else {
                    null
                }
            }
            .groupBy({ it.second }, { it.first })

        val patternsSetTrees = multiPatternTextsByPatterns.entries
            .sortedBy { it.key.joinToString(", ") }
            .map { (patterns, texts) ->
                val patternsLabel = patterns.joinToString(", ")
                val textChildren = texts.sorted().map { text ->
                    Tree(text)
                }
                Tree("{$patternsLabel} (${texts.size} total)", textChildren)
            }

        val totalMultiPatternTexts = multiPatternTextsByPatterns.values.sumOf { it.size }
        return Tree("multi-pattern-matches ($totalMultiPatternTexts total)", patternsSetTrees)
    }

    // Section 3: by-text
    // Hierarchy: text -> quantity, type, pattern
    private fun buildByTextSection(events: List<MatchedFilterEvent>): Tree {
        val eventsByText = events.groupBy { it.text }

        val textTrees = eventsByText.entries
            .sortedBy { it.key }
            .map { (text, eventsForText) ->
                // Count occurrences of each (type, pattern) for this text
                data class TypePatternKey(val type: String, val pattern: String)

                val typePatternCounts = eventsForText
                    .groupingBy { TypePatternKey(it.type, it.pattern) }
                    .eachCount()

                val typePatternChildren = typePatternCounts.entries
                    .sortedWith(
                        compareByDescending<Map.Entry<TypePatternKey, Int>> { it.value }
                            .thenBy { it.key.type }
                            .thenBy { it.key.pattern }
                    )
                    .map { (key, count) ->
                        Tree("$count: ${key.type}, ${key.pattern}")
                    }

                val totalCount = eventsForText.size
                val uniqueTypes = typePatternCounts.keys.map { it.type }.distinct()
                val typeCountLabel = if (uniqueTypes.size > 1) ", ${uniqueTypes.size} types" else ""
                Tree("$text ($totalCount total$typeCountLabel)", typePatternChildren)
            }

        val totalMatched = events.size
        return Tree("by-text ($totalMatched total)", textTrees)
    }

    // Section 4: by-pattern
    // Hierarchy: type, pattern -> quantity, text
    private fun buildByPatternSection(events: List<MatchedFilterEvent>): Tree {
        // Group by (type, pattern)
        data class TypePatternKey(val type: String, val pattern: String)

        val eventsByTypePattern = events.groupBy { TypePatternKey(it.type, it.pattern) }

        val typePatternTrees = eventsByTypePattern.entries
            .sortedWith(compareBy({ it.key.type }, { it.key.pattern }))
            .map { (key, eventsForTypePattern) ->
                // Count occurrences of each text under this (type, pattern)
                val textCounts = eventsForTypePattern.groupingBy { it.text }.eachCount()

                val textChildren = textCounts.entries
                    .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }
                        .thenBy { it.key })
                    .map { (text, count) ->
                        Tree("$count: $text")
                    }

                val totalCount = eventsForTypePattern.size
                Tree("${key.type}, ${key.pattern} ($totalCount total)", textChildren)
            }

        val totalMatched = events.size
        return Tree("by-pattern ($totalMatched total)", typePatternTrees)
    }

    // Section 5: no-matches
    // Flat list: text (sorted ascending)
    private fun buildNoMatchesSection(events: List<UnmatchedFilterEvent>): Tree {
        val uniqueTexts = events.map { it.text }.distinct().sorted()

        val textTrees = uniqueTexts.map { text ->
            Tree(text)
        }

        val totalUnmatched = events.size
        return Tree("no-matches ($totalUnmatched total)", textTrees)
    }
}
