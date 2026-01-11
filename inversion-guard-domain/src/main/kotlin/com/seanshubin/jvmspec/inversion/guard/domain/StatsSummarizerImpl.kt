package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.filter.FilterEvent
import com.seanshubin.jvmspec.domain.stats.Stats
import com.seanshubin.jvmspec.domain.tree.Tree
import java.nio.file.Path

class StatsSummarizerImpl(
    private val outputDir: Path
) : StatsSummarizer {
    override fun summarize(stats: Stats): List<Command> {
        // Level 1: Group by category
        val eventsByCategory = stats.filterEvents.groupBy { it.category }

        // Create a separate file command for each category
        val commands = eventsByCategory.entries
            .sortedBy { it.key }  // Sort categories alphabetically
            .map { (category, categoryEvents) ->
                val categoryTree = buildCategoryTree(category, categoryEvents)
                val path = outputDir.resolve("stats-$category.txt")
                CreateFileCommand(path, listOf(categoryTree))
            }

        return commands
    }

    private fun buildCategoryTree(category: String, events: List<FilterEvent>): Tree {
        // Level 2: Group by types set
        val eventsByTypesSet = events.groupBy { it.types }

        val typesSetTrees = eventsByTypesSet.entries
            .sortedBy { formatTypesSet(it.key) }  // Sort by formatted types set
            .map { (typesSet, typesSetEvents) ->
                buildTypesSetTree(typesSet, typesSetEvents)
            }

        val totalCount = events.size
        return Tree("$category ($totalCount total)", typesSetTrees)
    }

    private fun buildTypesSetTree(typesSet: Set<String>, events: List<FilterEvent>): Tree {
        val formattedTypes = formatTypesSet(typesSet)

        // Level 3: Build both branches
        val byPatternBranch = buildByPatternBranch(events)
        val byTextBranch = buildByTextBranch(events)

        val totalCount = events.size
        return Tree("$formattedTypes ($totalCount total)", listOf(byPatternBranch, byTextBranch))
    }

    private fun buildByPatternBranch(events: List<FilterEvent>): Tree {
        // Level 4: Group by pattern
        val eventsByPattern = events.groupBy { it.pattern }

        val patternTrees = eventsByPattern.entries
            .sortedBy { it.key }  // Sort patterns alphabetically
            .map { (pattern, patternEvents) ->
                // Level 5: Group by text to count occurrences
                val textCounts = patternEvents.groupingBy { it.text }.eachCount()

                val textChildren = textCounts.entries
                    .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }
                        .thenBy { it.key })  // Sort by count desc, then text asc
                    .map { (text, count) ->
                        Tree("$count: $text")
                    }

                val totalCount = patternEvents.size
                Tree("$pattern ($totalCount total)", textChildren)
            }

        return Tree("by-pattern", patternTrees)
    }

    private fun buildByTextBranch(events: List<FilterEvent>): Tree {
        // Level 4: Group by text
        val eventsByText = events.groupBy { it.text }

        val textTrees = eventsByText.entries
            .sortedWith(compareByDescending<Map.Entry<String, List<FilterEvent>>> { it.value.size }
                .thenBy { it.key })  // Sort by count desc, then text asc
            .map { (text, textEvents) ->
                // Level 5: Group by pattern to count occurrences
                val patternCounts = textEvents.groupingBy { it.pattern }.eachCount()

                val patternChildren = patternCounts.entries
                    .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }
                        .thenBy { it.key })  // Sort by count desc, then pattern asc
                    .map { (pattern, count) ->
                        Tree("$count: $pattern")
                    }

                val totalCount = textEvents.size
                Tree("$text ($totalCount total)", patternChildren)
            }

        return Tree("by-text", textTrees)
    }

    private fun formatTypesSet(types: Set<String>): String {
        // Sort types alphabetically and format with braces
        val sortedTypes = types.sorted().joinToString(", ")
        return "{$sortedTypes}"
    }
}
