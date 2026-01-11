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
        val byTextSection = buildByTextSection(matchedEvents)
        val byPatternSection = buildByPatternSection(matchedEvents)
        val noMatchesSection = buildNoMatchesSection(unmatchedEvents)

        val totalMatched = matchedEvents.size
        val totalUnmatched = unmatchedEvents.size
        val total = totalMatched + totalUnmatched

        return Tree(
            "$category ($total total: $totalMatched matched, $totalUnmatched unmatched)",
            listOf(multiTypeMatchesSection, byTextSection, byPatternSection, noMatchesSection)
        )
    }

    // Section 1: multi-type-matches
    // Flat list: text with types summary
    private fun buildMultiTypeMatchesSection(events: List<MatchedFilterEvent>): Tree {
        val eventsByText = events.groupBy { it.text }

        val multiTypeTexts = eventsByText.entries
            .filter { (_, eventsForText) ->
                // Only include texts that match multiple distinct types
                val uniqueTypes = eventsForText.map { it.type }.distinct()
                uniqueTypes.size > 1
            }
            .sortedBy { it.key }
            .map { (text, eventsForText) ->
                val uniqueTypes = eventsForText.map { it.type }.distinct().sorted()
                val typesLabel = uniqueTypes.joinToString(", ")
                Tree("$text (${uniqueTypes.size} types: $typesLabel)")
            }

        return Tree("multi-type-matches (${multiTypeTexts.size} total)", multiTypeTexts)
    }

    // Section 2: by-text
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

    // Section 3: by-pattern
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

    // Section 4: no-matches
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
