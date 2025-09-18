package com.seanshubin.jvmspec.domain.aggregation

data class AggregateData(val classDataMap: Map<String, ClassData>) {
    fun invokeStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): AggregateData {
        val key = source.toAggregateKey()
        return updateEntry(key) { classData ->
            classData.addToStaticReferenceCount(1)
        }
    }

    fun getStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): AggregateData {
        val key = source.toAggregateKey()
        return updateEntry(key) { classData ->
            classData.addToStaticReferenceCount(1)
        }
    }

    fun putStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): AggregateData {
        val key = source.toAggregateKey()
        return updateEntry(key) { classData ->
            classData.addToStaticReferenceCount(1)
        }
    }

    fun newInstance(
        source: QualifiedMethod,
        targetClassName: String
    ): AggregateData {
        val key = source.toAggregateKey()
        return updateEntry(key) { classData ->
            classData.addToStaticReferenceCount(1)
        }
    }

    fun cyclomaticComplexity(
        qualifiedMethod: QualifiedMethod,
        complexity: Int
    ): AggregateData {
        val key = qualifiedMethod.toAggregateKey()
        return updateEntry(key) { classData ->
            classData.addToCyclomaticComplexity(complexity)
        }
    }

    fun summaryDescendingCyclomaticComplexity(): List<String> {
        return classDataMap.values.sortedByDescending { it.cyclomaticComplexity }.map { it.toLine() }
    }

    fun summaryDescendingStaticReferenceCount(): List<String> {
        return classDataMap.values.sortedByDescending { it.staticReferenceCount }.map { it.toLine() }
    }

    private fun updateEntry(key: String, update: (ClassData) -> ClassData): AggregateData {
        val oldValue = classDataMap[key] ?: ClassData.create(key)
        val newValue = update(oldValue)
        val newMap = classDataMap + (key to newValue)
        return AggregateData(newMap)
    }

    companion object {
        val empty = AggregateData(emptyMap())
    }
}