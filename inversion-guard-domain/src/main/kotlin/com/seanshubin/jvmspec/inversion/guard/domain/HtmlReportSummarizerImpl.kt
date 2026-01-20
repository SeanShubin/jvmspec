package com.seanshubin.jvmspec.inversion.guard.domain

import java.nio.file.Path

class HtmlReportSummarizerImpl(
    private val outputDir: Path
) : HtmlReportSummarizer {
    override fun summarize(analysisList: List<ClassAnalysis>): List<Command> {
        val staticInvocationsThatShouldBeInverted = countBoundaryInvocationsInNonBoundaryMethods(analysisList)

        val indexHtml = generateIndexHtml(staticInvocationsThatShouldBeInverted)
        val redirectHtml = generateRedirectHtml()

        val indexCommand = CreateTextFileCommand(outputDir.resolve("index.html"), indexHtml)
        val redirectCommand = CreateTextFileCommand(outputDir.resolve("_index.html"), redirectHtml)

        return listOf(indexCommand, redirectCommand)
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
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Quality Metrics Report</title>
                <style>
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
                        line-height: 1.6;
                        max-width: 1200px;
                        margin: 0 auto;
                        padding: 20px;
                        background-color: #f5f5f5;
                    }
                    h1 {
                        color: #333;
                        border-bottom: 3px solid #007bff;
                        padding-bottom: 10px;
                    }
                    h2 {
                        color: #444;
                        margin-top: 30px;
                        border-bottom: 2px solid #ddd;
                        padding-bottom: 8px;
                    }
                    nav {
                        background-color: #fff;
                        padding: 20px;
                        border-radius: 5px;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                        margin-bottom: 30px;
                    }
                    nav h2 {
                        margin-top: 0;
                        border-bottom: none;
                    }
                    nav ul {
                        list-style-type: none;
                        padding-left: 0;
                    }
                    nav li {
                        margin: 8px 0;
                    }
                    nav a {
                        color: #007bff;
                        text-decoration: none;
                        font-weight: 500;
                    }
                    nav a:hover {
                        text-decoration: underline;
                    }
                    section {
                        background-color: #fff;
                        padding: 20px;
                        border-radius: 5px;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                        margin-bottom: 20px;
                    }
                    table {
                        width: 100%;
                        border-collapse: collapse;
                        margin-top: 15px;
                    }
                    th, td {
                        text-align: left;
                        padding: 12px;
                        border-bottom: 1px solid #ddd;
                    }
                    th {
                        background-color: #007bff;
                        color: white;
                        font-weight: 600;
                    }
                    tr:hover {
                        background-color: #f8f9fa;
                    }
                    .metric-value {
                        font-weight: bold;
                        color: ${if (staticInvocationsThatShouldBeInverted > 0) "#dc3545" else "#28a745"};
                    }
                </style>
            </head>
            <body>
                <h1>Quality Metrics Report</h1>

                <nav>
                    <h2>Table of Contents</h2>
                    <ul>
                        <li><a href="#quality-metrics">Quality Metrics</a></li>
                    </ul>
                </nav>

                <section id="quality-metrics">
                    <h2>Quality Metrics</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>Metric</th>
                                <th>Value</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>staticInvocationsThatShouldBeInverted</td>
                                <td class="metric-value">$staticInvocationsThatShouldBeInverted</td>
                            </tr>
                        </tbody>
                    </table>
                </section>
            </body>
            </html>
        """.trimIndent()
    }

    private fun generateRedirectHtml(): String {
        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta http-equiv="refresh" content="0; url=index.html">
                <title>Redirecting...</title>
            </head>
            <body>
                <p>Redirecting to <a href="index.html">index.html</a>...</p>
            </body>
            </html>
        """.trimIndent()
    }
}
