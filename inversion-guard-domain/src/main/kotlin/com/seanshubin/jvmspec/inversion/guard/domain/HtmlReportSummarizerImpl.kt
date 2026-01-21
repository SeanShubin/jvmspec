package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Companion.text
import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Tag
import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Text
import java.nio.charset.StandardCharsets
import java.nio.file.Path

class HtmlReportSummarizerImpl(
    private val outputDir: Path
) : HtmlReportSummarizer {
    private val classLoader = javaClass.classLoader

    override fun summarize(analysisList: List<ClassAnalysis>): List<Command> {
        val staticInvocationsThatShouldBeInverted = countBoundaryInvocationsInNonBoundaryMethods(analysisList)

        val indexHtml = generateIndexHtml(staticInvocationsThatShouldBeInverted)
        val indexCommand = CreateTextFileCommand(outputDir.resolve("index.html"), indexHtml)

        val cssCommand = loadResourceAsCommand("quality-metrics.css")
        val redirectCommand = loadResourceAsCommand("_index.html")

        return listOf(indexCommand, cssCommand, redirectCommand)
    }

    private fun countBoundaryInvocationsInNonBoundaryMethods(analysisList: List<ClassAnalysis>): Int {
        return analysisList.sumOf { classAnalysis ->
            classAnalysis.methodAnalysisList
                .filter { !it.isBoundaryLogic() }
                .sumOf { methodAnalysis ->
                    methodAnalysis.staticInvocations.count { invocation ->
                        invocation.invocationType == InvocationType.BOUNDARY
                    }
                }
        }
    }

    private fun generateIndexHtml(staticInvocationsThatShouldBeInverted: Int): String {
        val html = Tag(
            "html",
            attributes = listOf("lang" to "en"),
            children = listOf(
                createHead(),
                createBody(staticInvocationsThatShouldBeInverted)
            )
        )
        val doctype = "<!DOCTYPE html>"
        val htmlLines = html.toLines()
        return (listOf(doctype) + htmlLines).joinToString("\n")
    }

    private fun createHead(): HtmlElement {
        return Tag(
            "head",
            Tag("meta", attributes = listOf("charset" to "UTF-8")),
            Tag(
                "meta",
                attributes = listOf("name" to "viewport", "content" to "width=device-width, initial-scale=1.0")
            ),
            text("title", "Quality Metrics Report"),
            Tag("link", attributes = listOf("rel" to "stylesheet", "href" to "quality-metrics.css"))
        )
    }

    private fun createBody(staticInvocationsThatShouldBeInverted: Int): HtmlElement {
        return Tag(
            "body",
            text("h1", "Quality Metrics Report"),
            createTableOfContents(),
            createQualityMetricsSection(staticInvocationsThatShouldBeInverted)
        )
    }

    private fun createTableOfContents(): HtmlElement {
        return Tag(
            "nav",
            text("h2", "Table of Contents"),
            Tag(
                "ul",
                Tag(
                    "li",
                    Tag(
                        "a",
                        attributes = listOf("href" to "#quality-metrics"),
                        children = listOf(Text("Quality Metrics"))
                    )
                )
            )
        )
    }

    private fun createQualityMetricsSection(staticInvocationsThatShouldBeInverted: Int): HtmlElement {
        val cssClass = if (staticInvocationsThatShouldBeInverted > 0) "has-problems" else "no-problems"
        return Tag(
            "section",
            attributes = listOf("id" to "quality-metrics"),
            children = listOf(
                text("h2", "Quality Metrics"),
                Tag(
                    "table",
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
                        Tag(
                            "tr",
                            text("td", "staticInvocationsThatShouldBeInverted"),
                            Tag(
                                "td",
                                attributes = listOf("class" to "metric-value $cssClass"),
                                children = listOf(Text(staticInvocationsThatShouldBeInverted.toString()))
                            )
                        )
                    )
                )
            )
        )
    }

    private fun loadResourceAsCommand(resourceName: String): Command {
        val resourcePath = "html-report/$resourceName"
        val inputStream = classLoader.getResourceAsStream(resourcePath)
            ?: throw RuntimeException("Unable to load resource: $resourcePath")
        val content = inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
        val path = outputDir.resolve(resourceName)
        return CreateTextFileCommand(path, content)
    }
}
