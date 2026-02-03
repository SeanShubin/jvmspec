package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.infrastructure.collections.Tree
import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Companion.text
import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Tag
import com.seanshubin.jvmspec.inversion.guard.domain.HtmlElement.Text
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.output.formatting.JvmSpecFormat
import java.nio.charset.StandardCharsets
import java.nio.file.Path

class HtmlReportSummarizerImpl(
    private val baseDir: Path,
    private val outputDir: Path,
    private val reportGenerator: QualityMetricsDetailReportGenerator,
    private val jvmSpecFormat: JvmSpecFormat
) : HtmlReportSummarizer {
    private val classLoader = javaClass.classLoader

    override fun summarize(analysisList: List<ClassAnalysis>): List<Command> {
        val detailReport = reportGenerator.generate(analysisList)
        val staticInvocationsThatShouldBeInverted = detailReport.classes.sumOf { it.problemCount }

        val classPathMap = analysisList.associate { analysis ->
            analysis.jvmClass.thisClassName to calculateDisassemblyPath(analysis.jvmClass)
        }

        val indexHtml = generateIndexHtml(staticInvocationsThatShouldBeInverted, detailReport, classPathMap)
        val indexCommand = CreateTextFileCommand(outputDir.resolve("index.html"), indexHtml)

        val disassemblyIndexHtml = generateDisassemblyIndexHtml(analysisList, classPathMap)
        val disassemblyIndexCommand = CreateTextFileCommand(outputDir.resolve("disassembly.html"), disassemblyIndexHtml)

        val classPageCommands = analysisList.map { analysis ->
            generateClassDisassemblyPage(analysis)
        }

        val cssCommand = loadResourceAsCommand("quality-metrics.css")
        val redirectCommand = loadResourceAsCommand("_index.html")

        return listOf(indexCommand, disassemblyIndexCommand) + classPageCommands + listOf(cssCommand, redirectCommand)
    }

    private fun generateIndexHtml(
        staticInvocationsThatShouldBeInverted: Int,
        detailReport: QualityMetricsDetailReport,
        classPathMap: Map<String, Path>
    ): String {
        val html = Tag(
            "html",
            attributes = listOf("lang" to "en"),
            children = listOf(
                createHead(),
                createBody(staticInvocationsThatShouldBeInverted, detailReport, classPathMap)
            )
        )
        val doctype = "<!DOCTYPE html>"
        val htmlLines = html.toLines()
        return (listOf(doctype) + htmlLines).joinToString("\n")
    }

    private fun generateDisassemblyIndexHtml(
        analysisList: List<ClassAnalysis>,
        classPathMap: Map<String, Path>
    ): String {
        val sortedAnalyses = analysisList.sortedBy { baseDir.relativize(it.jvmClass.origin).toString() }

        val classListItems = sortedAnalyses.map { analysis ->
            val className = analysis.jvmClass.thisClassName
            val origin = baseDir.relativize(analysis.jvmClass.origin).toString()
            val relativePath = classPathMap[className]?.toString() ?: "$className-disassembly.html"

            Tag(
                "li",
                Tag(
                    "a",
                    attributes = listOf("href" to relativePath, "class" to "class-link"),
                    children = listOf(Text(origin))
                )
            )
        }

        val html = Tag(
            "html",
            attributes = listOf("lang" to "en"),
            children = listOf(
                createDisassemblyIndexHead(),
                createDisassemblyIndexBody(classListItems)
            )
        )

        val doctype = "<!DOCTYPE html>"
        val htmlLines = html.toLines()
        return (listOf(doctype) + htmlLines).joinToString("\n")
    }

    private fun createDisassemblyIndexHead(): HtmlElement {
        return Tag(
            "head",
            Tag("meta", attributes = listOf("charset" to "UTF-8")),
            Tag(
                "meta",
                attributes = listOf("name" to "viewport", "content" to "width=device-width, initial-scale=1.0")
            ),
            text("title", "Disassembly Index"),
            Tag("link", attributes = listOf("rel" to "stylesheet", "href" to "quality-metrics.css"))
        )
    }

    private fun createDisassemblyIndexBody(classListItems: List<HtmlElement>): HtmlElement {
        return Tag(
            "body",
            text("h1", "Disassembly Index"),
            Tag(
                "nav",
                attributes = listOf("class" to "back-navigation"),
                children = listOf(
                    Tag(
                        "a",
                        attributes = listOf("href" to "index.html"),
                        children = listOf(Text("← Back to Quality Metrics Report"))
                    )
                )
            ),
            Tag(
                "section",
                text("h2", "All Classes"),
                Tag(
                    "ul",
                    attributes = listOf("class" to "disassembly-index-list"),
                    children = classListItems
                )
            )
        )
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
        detailReport: QualityMetricsDetailReport,
        classPathMap: Map<String, Path>
    ): HtmlElement {
        return Tag(
            "body",
            text("h1", "Quality Metrics Report"),
            createTableOfContents(),
            createQualityMetricsSection(staticInvocationsThatShouldBeInverted, detailReport, classPathMap)
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
                ),
                Tag(
                    "li",
                    Tag(
                        "a",
                        attributes = listOf("href" to "filters.html"),
                        children = listOf(Text("Filter Statistics"))
                    )
                ),
                Tag(
                    "li",
                    Tag(
                        "a",
                        attributes = listOf("href" to "disassembly.html"),
                        children = listOf(Text("Disassembly Index"))
                    )
                )
            )
        )
    }

    private fun createQualityMetricsSection(
        staticInvocationsThatShouldBeInverted: Int,
        detailReport: QualityMetricsDetailReport,
        classPathMap: Map<String, Path>
    ): HtmlElement {
        val cssClass = if (staticInvocationsThatShouldBeInverted > 0) "has-problems" else "no-problems"
        return Tag(
            "section",
            attributes = listOf("id" to "quality-metrics"),
            children = listOf(
                text("h2", "Quality Metrics"),
                createSummaryTable(staticInvocationsThatShouldBeInverted, cssClass),
                createDetailTable(detailReport, classPathMap)
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

    private fun createDetailTable(
        detailReport: QualityMetricsDetailReport,
        classPathMap: Map<String, Path>
    ): HtmlElement {
        if (detailReport.classes.isEmpty()) {
            return Tag("p", Text("No quality metric violations found."))
        }

        val rows = detailReport.classes.flatMap { classDetail ->
            createClassRows(classDetail, classPathMap)
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

    private fun createClassRows(
        classDetail: QualityMetricsClassDetail,
        classPathMap: Map<String, Path>
    ): List<HtmlElement> {
        val relativePath =
            classPathMap[classDetail.className]?.toString() ?: "${classDetail.className}-disassembly.html"

        val classRow = Tag(
            "tr",
            attributes = listOf("class" to "class-row"),
            children = listOf(
                text("td", "Class"),
                Tag(
                    "td",
                    attributes = listOf("class" to "class-name"),
                    children = listOf(
                        Tag(
                            "a",
                            attributes = listOf("href" to relativePath),
                            children = listOf(Text(classDetail.className))
                        )
                    )
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

    private fun calculateDisassemblyPath(jvmClass: JvmClass): Path {
        val relativePath = baseDir.relativize(jvmClass.origin).parent
        val classFileName = jvmClass.origin.fileName.toString().removeSuffix(".class")
        return relativePath.resolve("$classFileName-disassembly.html")
    }

    private fun calculateCssRelativePath(htmlPath: Path): String {
        val depth = htmlPath.nameCount - 1 // -1 because we don't count the filename itself
        return if (depth == 0) {
            "quality-metrics.css"
        } else {
            "../".repeat(depth) + "quality-metrics.css"
        }
    }

    private fun treeToHtml(tree: Tree): HtmlElement {
        return if (tree.children.isEmpty()) {
            Tag("div", attributes = listOf("class" to "tree-leaf"), children = listOf(Text(tree.node)))
        } else {
            Tag(
                "details",
                Tag("summary", Text(tree.node)),
                *tree.children.map { treeToHtml(it) }.toTypedArray()
            )
        }
    }

    private fun generateClassDisassemblyPage(analysis: ClassAnalysis): Command {
        val trees = jvmSpecFormat.classTreeList(analysis.jvmClass)
        val relativePath = calculateDisassemblyPath(analysis.jvmClass)
        val cssPath = calculateCssRelativePath(relativePath)
        val indexPath = calculateCssRelativePath(relativePath).replace("quality-metrics.css", "index.html")
        val textFileName = relativePath.fileName.toString().replace("-disassembly.html", "-disassembled.txt")
        val html =
            generateClassDisassemblyHtml(analysis.jvmClass.thisClassName, trees, cssPath, indexPath, textFileName)

        val path = outputDir.resolve(relativePath)
        return CreateTextFileCommand(path, html)
    }

    private fun generateClassDisassemblyHtml(
        className: String,
        trees: List<Tree>,
        cssPath: String,
        indexPath: String,
        textFilePath: String
    ): String {
        val html = Tag(
            "html",
            attributes = listOf("lang" to "en"),
            children = listOf(
                createClassPageHead(className, cssPath),
                createClassPageBody(className, trees, indexPath, textFilePath)
            )
        )
        val doctype = "<!DOCTYPE html>"
        val htmlLines = html.toLines()
        return (listOf(doctype) + htmlLines).joinToString("\n")
    }

    private fun createClassPageHead(className: String, cssPath: String): HtmlElement {
        return Tag(
            "head",
            Tag("meta", attributes = listOf("charset" to "UTF-8")),
            Tag(
                "meta",
                attributes = listOf("name" to "viewport", "content" to "width=device-width, initial-scale=1.0")
            ),
            text("title", "Disassembly: $className"),
            Tag("link", attributes = listOf("rel" to "stylesheet", "href" to cssPath))
        )
    }

    private fun createClassPageBody(
        className: String,
        trees: List<Tree>,
        indexPath: String,
        textFilePath: String
    ): HtmlElement {
        val treeElements = trees.map { treeToHtml(it) }

        return Tag(
            "body",
            text("h1", "Class Disassembly: $className"),
            createBackLink(indexPath),
            createTextFileLink(textFilePath),
            Tag(
                "section",
                attributes = listOf("id" to "disassembly", "class" to "disassembly-section"),
                children = treeElements
            )
        )
    }

    private fun createBackLink(indexPath: String): HtmlElement {
        return Tag(
            "nav",
            attributes = listOf("class" to "back-navigation"),
            children = listOf(
                Tag(
                    "a",
                    attributes = listOf("href" to indexPath),
                    children = listOf(Text("← Back to Quality Metrics Report"))
                )
            )
        )
    }

    private fun createTextFileLink(textFilePath: String): HtmlElement {
        return Tag(
            "nav",
            attributes = listOf("class" to "back-navigation"),
            children = listOf(
                Tag(
                    "a",
                    attributes = listOf("href" to textFilePath),
                    children = listOf(Text("View Raw Text Disassembly"))
                )
            )
        )
    }
}
