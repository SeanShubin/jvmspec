package com.seanshubin.jvmspec.inversion.guard.domain

data class QualityMetricsDetailReport(
    val classes: List<QualityMetricsClassDetail>
)

data class QualityMetricsClassDetail(
    val className: String,
    val problemCount: Int,
    val complexity: Int,
    val methods: List<QualityMetricsMethodDetail>
)

data class QualityMetricsMethodDetail(
    val methodSignature: String,
    val complexity: Int,
    val isBoundaryLogic: Boolean,
    val boundaryLogicCategories: List<String>,
    val invocations: List<QualityMetricsInvocationDetail>
)

data class QualityMetricsInvocationDetail(
    val invocationType: String,
    val opcode: String,
    val className: String,
    val methodName: String,
    val signature: String
)
