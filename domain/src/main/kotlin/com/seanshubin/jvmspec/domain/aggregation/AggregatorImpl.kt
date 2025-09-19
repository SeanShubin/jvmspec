package com.seanshubin.jvmspec.domain.aggregation

import com.seanshubin.jvmspec.domain.util.MatchEnum

class AggregatorImpl(private val initialAggregateData: AggregateData) : Aggregator {
    private var aggregateData: AggregateData = initialAggregateData
    override fun invokeStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ) {
        aggregateData = aggregateData.invokeStatic(source, target)
    }

    override fun getStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ) {
        aggregateData = aggregateData.getStatic(source, target)
    }

    override fun putStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ) {
        aggregateData = aggregateData.putStatic(source, target)
    }

    override fun newInstance(
        source: QualifiedMethod,
        targetClassName: String
    ) {
        aggregateData = aggregateData.newInstance(source, targetClassName)
    }

    override fun cyclomaticComplexity(
        qualifiedMethod: QualifiedMethod,
        complexity: Int
    ) {
        aggregateData = aggregateData.cyclomaticComplexity(qualifiedMethod, complexity)
    }

    override fun summaryDescendingCyclomaticComplexity(): List<String> {
        return aggregateData.summaryDescendingCyclomaticComplexity()
    }

    override fun summaryDescendingStaticReferenceCount(): List<String> {
        return aggregateData.summaryDescendingStaticReferenceCount()
    }

    override fun summaryMethodNames(matchEnum: MatchEnum): List<String> {
        return aggregateData.summaryMethodNames(matchEnum)
    }

    override fun summaryClassNames(matchEnum: MatchEnum): List<String> {
        return aggregateData.summaryClassNames(matchEnum)
    }
}
