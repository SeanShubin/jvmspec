package com.seanshubin.jvmspec.domain.aggregation

interface Aggregator {
    fun invokeStatic(source: QualifiedMethod, target: QualifiedMethod)
    fun getStatic(source: QualifiedMethod, target: QualifiedMethod)
    fun putStatic(source: QualifiedMethod, target: QualifiedMethod)
    fun newInstance(source: QualifiedMethod, targetClassName: String)
    fun cyclomaticComplexity(qualifiedMethod: QualifiedMethod, complexity: Int)
    fun summaryDescendingCyclomaticComplexity(): List<String>
    fun summaryDescendingStaticReferenceCount(): List<String>
}
