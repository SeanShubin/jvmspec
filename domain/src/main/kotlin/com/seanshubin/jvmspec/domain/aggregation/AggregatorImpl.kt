package com.seanshubin.jvmspec.domain.aggregation

import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.jvm.JvmMethod
import com.seanshubin.jvmspec.domain.jvm.JvmRef
import com.seanshubin.jvmspec.domain.util.MatchEnum

class AggregatorImpl(private val initialAggregateData: AggregateData) : Aggregator {
    private var aggregateData: AggregateData = initialAggregateData
    override fun classFile(classFile: JvmClass) {
        aggregateData = aggregateData.classFile(classFile)
    }

    override fun invokeStatic(
        source: JvmMethod,
        target: JvmRef
    ) {
        aggregateData = aggregateData.invokeStatic(source, target)
    }

    override fun getStatic(
        source: JvmMethod,
        target: JvmRef
    ) {
        aggregateData = aggregateData.getStatic(source, target)
    }

    override fun putStatic(
        source: JvmMethod,
        target: JvmRef
    ) {
        aggregateData = aggregateData.putStatic(source, target)
    }

    override fun newInstance(
        source: JvmMethod,
        targetClassName: String
    ) {
        aggregateData = aggregateData.newInstance(source, targetClassName)
    }

    override fun cyclomaticComplexity(
        source: JvmMethod,
        complexity: Int
    ) {
        aggregateData = aggregateData.cyclomaticComplexity(source, complexity)
    }

    override fun summaryDescendingCyclomaticComplexity(): List<String> {
        return aggregateData.summaryDescendingCyclomaticComplexity()
    }

    override fun summaryDescendingStaticReferenceCount(): List<String> {
        return aggregateData.summaryDescendingStaticReferenceCount()
    }

    override fun summaryTotal(): List<String> {
        return aggregateData.totals()
    }

    override fun summaryMethodNames(matchEnum: MatchEnum): List<String> {
        return aggregateData.summaryMethodNames(matchEnum)
    }

    override fun summaryClassNames(matchEnum: MatchEnum): List<String> {
        return aggregateData.summaryClassNames(matchEnum)
    }

    override fun summaryOrigin(): List<String> {
        return aggregateData.summaryOrigin()
    }

    override fun methodCategories(
        method: JvmMethod,
        categories: Set<String>
    ) {
        aggregateData = aggregateData.methodCategories(method, categories)
    }
}
