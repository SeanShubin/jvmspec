package com.seanshubin.jvmspec.inversion.guard.domain

interface QualityMetricsDetailReportGenerator {
    fun generate(analysisList: List<ClassAnalysis>): QualityMetricsDetailReport
}

class QualityMetricsDetailReportGeneratorImpl : QualityMetricsDetailReportGenerator {
    override fun generate(analysisList: List<ClassAnalysis>): QualityMetricsDetailReport {
        val classDetails = analysisList
            .filter { it.countProblems() > 0 }
            .map { classAnalysis ->
                val methodDetails = classAnalysis.methodAnalysisList
                    .filter { !it.isBoundaryLogic() && it.staticInvocations.isNotEmpty() }
                    .map { methodAnalysis ->
                        val invocationDetails = methodAnalysis.staticInvocations
                            .map { invocation ->
                                QualityMetricsInvocationDetail(
                                    invocationType = invocation.invocationType.name,
                                    opcode = invocation.invocationOpcodeName,
                                    className = invocation.className,
                                    methodName = invocation.methodName,
                                    signature = invocation.signature.compactFormat()
                                )
                            }

                        QualityMetricsMethodDetail(
                            methodSignature = formatMethodSignature(methodAnalysis),
                            complexity = methodAnalysis.complexity,
                            isBoundaryLogic = methodAnalysis.isBoundaryLogic(),
                            boundaryLogicCategories = methodAnalysis.boundaryLogicCategories.sorted(),
                            invocations = invocationDetails
                        )
                    }
                    .sortedWith(
                        compareByDescending<QualityMetricsMethodDetail> { it.invocations.size }
                            .thenBy { it.methodSignature }
                    )

                QualityMetricsClassDetail(
                    className = classAnalysis.jvmClass.thisClassName,
                    problemCount = classAnalysis.countProblems(),
                    complexity = classAnalysis.complexity(),
                    methods = methodDetails
                )
            }
            .sortedWith(
                compareByDescending<QualityMetricsClassDetail> { it.problemCount }
                    .thenBy { it.className }
            )

        return QualityMetricsDetailReport(classes = classDetails)
    }

    private fun formatMethodSignature(methodAnalysis: MethodAnalysis): String {
        return methodAnalysis.signature.javaFormat()
    }
}
