package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.filter.FilterEvent
import com.seanshubin.jvmspec.domain.stats.Stats
import com.seanshubin.jvmspec.domain.tree.Tree
import java.nio.file.Path

class StatsSummarizerImpl(
    private val outputDir: Path
) : StatsSummarizer {
    override fun summarize(stats: Stats): List<Command> {
        val groupedByText = stats.filterEvents.groupBy { it.text }
        val reportTrees = groupedByText.entries
            .sortedByDescending { it.value.size }
            .map { (text, events) -> createTextReport(text, events) }
        val path = outputDir.resolve("stats.txt")
        val createFileCommand = CreateFileCommand(path, reportTrees)
        return listOf(createFileCommand)
    }

    private fun createTextReport(text: String, events: List<FilterEvent>): Tree {
        data class EventKey(val category: String, val type: String, val pattern: String)

        val groupedByKey = events.groupBy { event ->
            EventKey(event.category, event.type, event.pattern)
        }

        val children = groupedByKey.entries
            .sortedWith(
                compareBy<Map.Entry<EventKey, List<FilterEvent>>>(
                    { it.key.category },
                    { it.key.type },
                    { it.key.pattern }
                )
            )
            .map { (key, eventList) ->
                val count = eventList.size
                Tree("$count: category=${key.category}, type=${key.type}, pattern=${key.pattern}")
            }

        val totalCount = events.size
        val header = "$text ($totalCount total)"
        return Tree(header, children)
    }
}
