package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Companion.text
import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Tag
import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Text
import java.nio.charset.StandardCharsets
import java.nio.file.Path

class HtmlReportSummarizerImpl(
    private val outputDir: Path,
    private val reportGenerator: QualityMetricsDetailReportGenerator
) : HtmlReportSummarizer {
    private val classLoader = javaClass.classLoader

    override fun summarize(analysisList: List<ClassAnalysis>): List<Command> {
        val detailReport = reportGenerator.generate(analysisList)
        val staticInvocationsThatShouldBeInverted = detailReport.classes.sumOf { it.problemCount }

        val indexHtml = generateIndexHtml(staticInvocationsThatShouldBeInverted, detailReport)
        val indexCommand = CreateTextFileCommand(outputDir.resolve("index.html"), indexHtml)

        val cssCommand = loadResourceAsCommand("quality-metrics.css")
        val redirectCommand = loadResourceAsCommand("_index.html")

        return listOf(indexCommand, cssCommand, redirectCommand)
    }

    private fun generateIndexHtml(
        staticInvocationsThatShouldBeInverted: Int,
        detailReport: QualityMetricsDetailReport
    ): String {
        val html = Tag(
            "html",
            attributes = listOf("lang" to "en"),
            children = listOf(
                createHead(),
                createBody(staticInvocationsThatShouldBeInverted, detailReport)
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

    private fun createBody(
        staticInvocationsThatShouldBeInverted: Int,
        detailReport: QualityMetricsDetailReport
    ): HtmlElement {
        return Tag(
            "body",
            text("h1", "Quality Metrics Report"),
            createTableOfContents(),
            createQualityMetricsSection(staticInvocationsThatShouldBeInverted, detailReport)
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

    private fun createQualityMetricsSection(
        staticInvocationsThatShouldBeInverted: Int,
        detailReport: QualityMetricsDetailReport
    ): HtmlElement {
        val cssClass = if (staticInvocationsThatShouldBeInverted > 0) "has-problems" else "no-problems"
        return Tag(
            "section",
            attributes = listOf("id" to "quality-metrics"),
            children = listOf(
                text("h2", "Quality Metrics"),
                createSummaryTable(staticInvocationsThatShouldBeInverted, cssClass),
                createDetailTable(detailReport)
            )
        )
    }

    private fun createSummaryTable(count: Int, cssClass: String): HtmlElement {
        return Tag(
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
                    Tag(
                        "tr",
                        text("td", "staticInvocationsThatShouldBeInverted"),
                        Tag(
                            "td",
                            attributes = listOf("class" to "metric-value $cssClass"),
                            children = listOf(Text(count.toString()))
                        )
                    )
                )
            )
        )
    }

    private fun createDetailTable(detailReport: QualityMetricsDetailReport): HtmlElement {
        if (detailReport.classes.isEmpty()) {
            return Tag("p", Text("No quality metric violations found."))
        }

        val rows = detailReport.classes.flatMap { classDetail ->
            createClassRows(classDetail)
        }

        return Tag(
            "table",
            attributes = listOf("class" to "detail-table"),
            children = listOf(
                createDetailTableHeader(),
                Tag("tbody", rows)
            )
        )
    }

    private fun createDetailTableHeader(): HtmlElement {
        return Tag(
            "thead",
            Tag(
                "tr",
                text("th", "Level"),
                text("th", "Name/Signature"),
                text("th", "Problem Count"),
                text("th", "Complexity"),
                text("th", "Details")
            )
        )
    }

    private fun createClassRows(classDetail: QualityMetricsClassDetail): List<HtmlElement> {
        val classRow = Tag(
            "tr",
            attributes = listOf("class" to "class-row"),
            children = listOf(
                text("td", "Class"),
                Tag(
                    "td",
                    attributes = listOf("class" to "class-name"),
                    children = listOf(Text(classDetail.className))
                ),
                Tag(
                    "td",
                    attributes = listOf("class" to "problem-count"),
                    children = listOf(Text(classDetail.problemCount.toString()))
                ),
                text("td", classDetail.complexity.toString()),
                text("td", "${classDetail.methods.size} method(s)")
            )
        )

        val methodRows = classDetail.methods.flatMap { methodDetail ->
            createMethodRows(methodDetail)
        }

        return listOf(classRow) + methodRows
    }

    private fun createMethodRows(methodDetail: QualityMetricsMethodDetail): List<HtmlElement> {
        val methodRow = Tag(
            "tr",
            attributes = listOf("class" to "method-row"),
            children = listOf(
                text("td", "Method"),
                Tag(
                    "td",
                    attributes = listOf("class" to "method-signature"),
                    children = listOf(Text(methodDetail.methodSignature))
                ),
                text("td", ""),
                text("td", methodDetail.complexity.toString()),
                text("td", "${methodDetail.invocations.size} invocation(s)")
            )
        )

        val invocationRows = methodDetail.invocations.map { invocationDetail ->
            createInvocationRow(invocationDetail)
        }

        return listOf(methodRow) + invocationRows
    }

    private fun createInvocationRow(invocationDetail: QualityMetricsInvocationDetail): HtmlElement {
        val typeClass = "invocation-type-${invocationDetail.invocationType.lowercase()}"
        return Tag(
            "tr",
            attributes = listOf("class" to "invocation-row $typeClass"),
            children = listOf(
                text("td", "Invocation"),
                Tag(
                    "td",
                    attributes = listOf("class" to "invocation-signature"),
                    children = listOf(Text(invocationDetail.signature))
                ),
                text("td", ""),
                text("td", ""),
                Tag(
                    "td",
                    attributes = listOf("class" to "invocation-details"),
                    children = listOf(Text("${invocationDetail.invocationType} ${invocationDetail.opcode}"))
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
