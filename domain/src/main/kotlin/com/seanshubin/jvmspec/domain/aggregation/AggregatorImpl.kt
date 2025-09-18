package com.seanshubin.jvmspec.domain.aggregation

class AggregatorImpl : Aggregator {
    private var aggregateData: AggregateData = AggregateData.empty
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
}