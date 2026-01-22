package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.analysis.filtering.MatchedFilterEvent
import com.seanshubin.jvmspec.domain.analysis.filtering.UnmatchedFilterEvent
import com.seanshubin.jvmspec.domain.analysis.statistics.Stats
import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Companion.text
import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Tag
import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Text
import java.nio.file.Path

interface HtmlStatsSummarizer {
    fun summarize(stats: Stats): List<Command>
}

class HtmlStatsSummarizerImpl(
    private val outputDir: Path
) : HtmlStatsSummarizer {
    override fun summarize(stats: Stats): List<Command> {
        val matchedByCategory = stats.matchedFilterEvents.groupBy { it.category }
        val unmatchedByCategory = stats.unmatchedFilterEvents.groupBy { it.category }
        val allCategories = (matchedByCategory.keys + unmatchedByCategory.keys).distinct().sorted()

        val indexCommand = createFilterIndexPage(allCategories, matchedByCategory, unmatchedByCategory)

        val categoryCommands = allCategories.flatMap { category ->
            val matchedEvents = matchedByCategory[category] ?: emptyList()
            val unmatchedEvents = unmatchedByCategory[category] ?: emptyList()
            val registeredLocalPatterns = stats.registeredLocalPatterns[category] ?: emptyMap()
            createCategoryPages(category, matchedEvents, unmatchedEvents, registeredLocalPatterns)
        }

        return listOf(indexCommand) + categoryCommands
    }

    private fun createFilterIndexPage(
        categories: List<String>,
        matchedByCategory: Map<String, List<MatchedFilterEvent>>,
        unmatchedByCategory: Map<String, List<UnmatchedFilterEvent>>
    ): Command {
        val categoryRows = categories.map { category ->
            val matched = matchedByCategory[category]?.size ?: 0
            val unmatched = unmatchedByCategory[category]?.size ?: 0
            val total = matched + unmatched

            Tag(
                "tr",
                text("td", category),
                Tag("td", Text(total.toString())),
                Tag("td", Text(matched.toString())),
                Tag("td", Text(unmatched.toString())),
                Tag(
                    "td",
                    Tag(
                        "a",
                        attributes = listOf("href" to "filters-$category.html"),
                        children = listOf(Text("View Details"))
                    )
                )
            )
        }

        val html = Tag(
            "html",
            attributes = listOf("lang" to "en"),
            children = listOf(
                createHead("Filter Statistics"),
                createBody(
                    "Filter Statistics",
                    createCategoryTable(categoryRows)
                )
            )
        )

        val doctype = "<!DOCTYPE html>"
        val htmlLines = html.toLines()
        val htmlContent = (listOf(doctype) + htmlLines).joinToString("\n")

        return CreateTextFileCommand(outputDir.resolve("filters.html"), htmlContent)
    }

    private fun createHead(title: String): HtmlElement {
        return Tag(
            "head",
            Tag("meta", attributes = listOf("charset" to "UTF-8")),
            Tag(
                "meta",
                attributes = listOf("name" to "viewport", "content" to "width=device-width, initial-scale=1.0")
            ),
            text("title", title),
            Tag("link", attributes = listOf("rel" to "stylesheet", "href" to "quality-metrics.css"))
        )
    }

    private fun createBody(heading: String, content: HtmlElement): HtmlElement {
        return Tag(
            "body",
            text("h1", heading),
            createBackToIndex(),
            Tag(
                "section",
                content
            )
        )
    }

    private fun createBackToIndex(): HtmlElement {
        return Tag(
            "nav",
            attributes = listOf("class" to "back-navigation"),
            children = listOf(
                Tag(
                    "a",
                    attributes = listOf("href" to "index.html"),
                    children = listOf(Text("← Back to Quality Metrics Report"))
                )
            )
        )
    }

    private fun createCategoryTable(rows: List<HtmlElement>): HtmlElement {
        return Tag(
            "table",
            attributes = listOf("class" to "detail-table"),
            children = listOf(
                Tag(
                    "thead",
                    Tag(
                        "tr",
                        text("th", "Filter Category"),
                        text("th", "Total"),
                        text("th", "Matched"),
                        text("th", "Unmatched"),
                        text("th", "Details")
                    )
                ),
                Tag("tbody", rows)
            )
        )
    }

    private fun createCategoryPages(
        category: String,
        matchedEvents: List<MatchedFilterEvent>,
        unmatchedEvents: List<UnmatchedFilterEvent>,
        registeredLocalPatterns: Map<String, List<String>>
    ): List<Command> {
        val sections = listOf(
            Triple(
                "multi-type-matches",
                buildMultiTypeMatchesTable(matchedEvents),
                countMultiTypeMatches(matchedEvents)
            ),
            Triple(
                "multi-pattern-matches",
                buildMultiPatternMatchesTable(matchedEvents),
                countMultiPatternMatches(matchedEvents)
            ),
            Triple("by-text", buildByTextTable(matchedEvents), countByText(matchedEvents)),
            Triple("by-pattern", buildByPatternTable(matchedEvents), countByPattern(matchedEvents)),
            Triple("unmatched-text", buildUnmatchedTextTable(unmatchedEvents), countUnmatchedText(unmatchedEvents)),
            Triple(
                "unused-local-patterns",
                buildUnusedLocalPatternsTable(matchedEvents, registeredLocalPatterns),
                countUnusedLocalPatterns(matchedEvents, registeredLocalPatterns)
            )
        )

        val categoryIndexCommand = createCategoryIndexPage(category, sections, matchedEvents, unmatchedEvents)

        val sectionCommands = sections.map { (sectionName, table, _) ->
            createSectionPage(category, sectionName, table)
        }

        return listOf(categoryIndexCommand) + sectionCommands
    }

    private fun createCategoryIndexPage(
        category: String,
        sections: List<Triple<String, HtmlElement, Int>>,
        matchedEvents: List<MatchedFilterEvent>,
        unmatchedEvents: List<UnmatchedFilterEvent>
    ): Command {
        val totalMatched = matchedEvents.size
        val totalUnmatched = unmatchedEvents.size
        val total = totalMatched + totalUnmatched

        val summaryTable = Tag(
            "table",
            attributes = listOf("class" to "summary-table"),
            children = listOf(
                Tag(
                    "thead",
                    Tag(
                        "tr",
                        text("th", "Metric"),
                        text("th", "Value")
                    )
                ),
                Tag(
                    "tbody",
                    Tag("tr", text("td", "Total"), Tag("td", Text(total.toString()))),
                    Tag("tr", text("td", "Matched"), Tag("td", Text(totalMatched.toString()))),
                    Tag("tr", text("td", "Unmatched"), Tag("td", Text(totalUnmatched.toString())))
                )
            )
        )

        val sectionLinks = sections.map { (sectionName, _, count) ->
            Tag(
                "li",
                Tag(
                    "a",
                    attributes = listOf("href" to "filters-$category-$sectionName.html"),
                    children = listOf(Text("${formatSectionName(sectionName)} ($count)"))
                )
            )
        }

        val content = Tag(
            "div",
            summaryTable,
            text("h2", "Sections"),
            Tag("ul", sectionLinks)
        )

        val html = Tag(
            "html",
            attributes = listOf("lang" to "en"),
            children = listOf(
                createHead("Filter: $category"),
                createBody("Filter: $category", content)
            )
        )

        val doctype = "<!DOCTYPE html>"
        val htmlLines = html.toLines()
        val htmlContent = (listOf(doctype) + htmlLines).joinToString("\n")

        return CreateTextFileCommand(outputDir.resolve("filters-$category.html"), htmlContent)
    }

    private fun createSectionPage(
        category: String,
        sectionName: String,
        table: HtmlElement
    ): Command {
        val backToCategoryLink = Tag(
            "nav",
            attributes = listOf("class" to "back-navigation"),
            children = listOf(
                Tag(
                    "a",
                    attributes = listOf("href" to "filters-$category.html"),
                    children = listOf(Text("← Back to $category"))
                )
            )
        )

        val content = Tag(
            "div",
            backToCategoryLink,
            table
        )

        val html = Tag(
            "html",
            attributes = listOf("lang" to "en"),
            children = listOf(
                createHead("${formatSectionName(sectionName)} - $category"),
                Tag(
                    "body",
                    text("h1", "${formatSectionName(sectionName)} - $category"),
                    createBackToIndex(),
                    Tag("section", content)
                )
            )
        )

        val doctype = "<!DOCTYPE html>"
        val htmlLines = html.toLines()
        val htmlContent = (listOf(doctype) + htmlLines).joinToString("\n")

        return CreateTextFileCommand(outputDir.resolve("filters-$category-$sectionName.html"), htmlContent)
    }

    private fun formatSectionName(sectionName: String): String {
        return sectionName.split("-").joinToString(" ") { it.replaceFirstChar { char -> char.uppercase() } }
    }

    private fun buildMultiTypeMatchesTable(events: List<MatchedFilterEvent>): HtmlElement {
        val eventsByText = events.groupBy { it.text }

        val multiTypeTexts = eventsByText.entries
            .mapNotNull { (text, eventsForText) ->
                val uniqueTypes = eventsForText.map { it.type }.distinct().sorted()
                if (uniqueTypes.size > 1) {
                    text to uniqueTypes
                } else {
                    null
                }
            }
            .sortedBy { it.first }

        if (multiTypeTexts.isEmpty()) {
            return Tag("p", Text("No multi-type matches found."))
        }

        val rows = multiTypeTexts.map { (text, types) ->
            Tag(
                "tr",
                Tag("td", attributes = listOf("class" to "filter-text"), children = listOf(Text(text))),
                Tag("td", Text(types.joinToString(", ")))
            )
        }

        return Tag(
            "table",
            attributes = listOf("class" to "detail-table"),
            children = listOf(
                Tag(
                    "thead",
                    Tag(
                        "tr",
                        text("th", "Text"),
                        text("th", "Types")
                    )
                ),
                Tag("tbody", rows)
            )
        )
    }

    private fun buildMultiPatternMatchesTable(events: List<MatchedFilterEvent>): HtmlElement {
        val eventsByText = events.groupBy { it.text }

        val multiPatternTexts = eventsByText.entries
            .mapNotNull { (text, eventsForText) ->
                val uniquePatterns = eventsForText.map { it.pattern }.distinct().sorted()
                if (uniquePatterns.size > 1) {
                    text to uniquePatterns
                } else {
                    null
                }
            }
            .sortedBy { it.first }

        if (multiPatternTexts.isEmpty()) {
            return Tag("p", Text("No multi-pattern matches found."))
        }

        val rows = multiPatternTexts.map { (text, patterns) ->
            Tag(
                "tr",
                Tag("td", attributes = listOf("class" to "filter-text"), children = listOf(Text(text))),
                Tag("td", Text(patterns.joinToString(", ")))
            )
        }

        return Tag(
            "table",
            attributes = listOf("class" to "detail-table"),
            children = listOf(
                Tag(
                    "thead",
                    Tag(
                        "tr",
                        text("th", "Text"),
                        text("th", "Patterns")
                    )
                ),
                Tag("tbody", rows)
            )
        )
    }

    private fun buildByTextTable(events: List<MatchedFilterEvent>): HtmlElement {
        val eventsByText = events.groupBy { it.text }

        if (eventsByText.isEmpty()) {
            return Tag("p", Text("No matches found."))
        }

        val rows = eventsByText.entries
            .sortedBy { it.key }
            .map { (text, eventsForText) ->
                data class TypePatternKey(val type: String, val pattern: String)

                val typePatternCounts = eventsForText
                    .groupingBy { TypePatternKey(it.type, it.pattern) }
                    .eachCount()

                val detailItems = typePatternCounts.entries
                    .sortedWith(
                        compareByDescending<Map.Entry<TypePatternKey, Int>> { it.value }
                            .thenBy { it.key.type }
                            .thenBy { it.key.pattern }
                    )
                    .map { (key, count) ->
                        Tag("li", Text("$count × (${key.type}, ${key.pattern})"))
                    }

                Tag(
                    "tr",
                    Tag("td", attributes = listOf("class" to "filter-text"), children = listOf(Text(text))),
                    Tag("td", Text(eventsForText.size.toString())),
                    Tag("td", Tag("ul", detailItems))
                )
            }

        return Tag(
            "table",
            attributes = listOf("class" to "detail-table"),
            children = listOf(
                Tag(
                    "thead",
                    Tag(
                        "tr",
                        text("th", "Text"),
                        text("th", "Count"),
                        text("th", "Type & Pattern Details")
                    )
                ),
                Tag("tbody", rows)
            )
        )
    }

    private fun buildByPatternTable(events: List<MatchedFilterEvent>): HtmlElement {
        data class TypePatternKey(val type: String, val pattern: String)

        val eventsByTypePattern = events.groupBy { TypePatternKey(it.type, it.pattern) }

        if (eventsByTypePattern.isEmpty()) {
            return Tag("p", Text("No matches found."))
        }

        val rows = eventsByTypePattern.entries
            .sortedWith(compareBy({ it.key.type }, { it.key.pattern }))
            .map { (key, eventsForTypePattern) ->
                val textCounts = eventsForTypePattern.groupingBy { it.text }.eachCount()

                val detailItems = textCounts.entries
                    .sortedWith(compareByDescending<Map.Entry<String, Int>> { it.value }
                        .thenBy { it.key })
                    .map { (text, count) ->
                        Tag(
                            "li",
                            attributes = listOf("class" to "filter-text"),
                            children = listOf(Text("$count × $text"))
                        )
                    }

                Tag(
                    "tr",
                    Tag("td", Text(key.type)),
                    Tag("td", attributes = listOf("class" to "filter-text"), children = listOf(Text(key.pattern))),
                    Tag("td", Text(eventsForTypePattern.size.toString())),
                    Tag("td", Tag("ul", detailItems))
                )
            }

        return Tag(
            "table",
            attributes = listOf("class" to "detail-table"),
            children = listOf(
                Tag(
                    "thead",
                    Tag(
                        "tr",
                        text("th", "Type"),
                        text("th", "Pattern"),
                        text("th", "Count"),
                        text("th", "Text Details")
                    )
                ),
                Tag("tbody", rows)
            )
        )
    }

    private fun buildUnmatchedTextTable(events: List<UnmatchedFilterEvent>): HtmlElement {
        val uniqueTexts = events.map { it.text }.distinct().sorted()

        if (uniqueTexts.isEmpty()) {
            return Tag("p", Text("All items matched at least one filter."))
        }

        val rows = uniqueTexts.map { text ->
            Tag(
                "tr",
                Tag("td", attributes = listOf("class" to "filter-text"), children = listOf(Text(text)))
            )
        }

        return Tag(
            "table",
            attributes = listOf("class" to "detail-table"),
            children = listOf(
                Tag(
                    "thead",
                    Tag("tr", text("th", "Text"))
                ),
                Tag("tbody", rows)
            )
        )
    }

    private fun buildUnusedLocalPatternsTable(
        events: List<MatchedFilterEvent>,
        registeredLocalPatterns: Map<String, List<String>>
    ): HtmlElement {
        data class TypePatternKey(val type: String, val pattern: String)

        val usedPatterns = events.map { TypePatternKey(it.type, it.pattern) }.toSet()

        val unusedLocalPatterns = registeredLocalPatterns.flatMap { (type, patterns) ->
            patterns.filter { pattern -> TypePatternKey(type, pattern) !in usedPatterns }
                .map { pattern -> TypePatternKey(type, pattern) }
        }.sortedWith(compareBy({ it.type }, { it.pattern }))

        if (unusedLocalPatterns.isEmpty()) {
            return Tag("p", Text("All local patterns matched at least one item."))
        }

        val rows = unusedLocalPatterns.map { (type, pattern) ->
            Tag(
                "tr",
                text("td", type),
                Tag("td", attributes = listOf("class" to "filter-text"), children = listOf(Text(pattern)))
            )
        }

        return Tag(
            "table",
            attributes = listOf("class" to "detail-table"),
            children = listOf(
                Tag(
                    "thead",
                    Tag(
                        "tr",
                        text("th", "Type"),
                        text("th", "Pattern")
                    )
                ),
                Tag("tbody", rows)
            )
        )
    }

    private fun countMultiTypeMatches(events: List<MatchedFilterEvent>): Int {
        val eventsByText = events.groupBy { it.text }
        return eventsByText.count { (_, eventsForText) ->
            eventsForText.map { it.type }.distinct().size > 1
        }
    }

    private fun countMultiPatternMatches(events: List<MatchedFilterEvent>): Int {
        val eventsByText = events.groupBy { it.text }
        return eventsByText.count { (_, eventsForText) ->
            eventsForText.map { it.pattern }.distinct().size > 1
        }
    }

    private fun countByText(events: List<MatchedFilterEvent>): Int {
        return events.map { it.text }.distinct().size
    }

    private fun countByPattern(events: List<MatchedFilterEvent>): Int {
        data class TypePatternKey(val type: String, val pattern: String)
        return events.map { TypePatternKey(it.type, it.pattern) }.distinct().size
    }

    private fun countUnmatchedText(events: List<UnmatchedFilterEvent>): Int {
        return events.map { it.text }.distinct().size
    }

    private fun countUnusedLocalPatterns(
        events: List<MatchedFilterEvent>,
        registeredLocalPatterns: Map<String, List<String>>
    ): Int {
        data class TypePatternKey(val type: String, val pattern: String)

        val usedPatterns = events.map { TypePatternKey(it.type, it.pattern) }.toSet()

        return registeredLocalPatterns.flatMap { (type, patterns) ->
            patterns.filter { pattern -> TypePatternKey(type, pattern) !in usedPatterns }
        }.size
    }
}
