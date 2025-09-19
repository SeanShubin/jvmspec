package com.seanshubin.jvmspec.domain.aggregation

import com.seanshubin.jvmspec.domain.util.MatchEnum

data class AggregateData(
    val classDataMap: Map<String, ClassData>,
    val acceptMethod: (QualifiedMethod) -> MatchEnum,
    val acceptClass: (String) -> MatchEnum,
    val methods: Map<MatchEnum, Set<QualifiedMethod>>,
    val classes: Map<MatchEnum, Set<String>>
) {
    fun invokeStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): AggregateData {
        return incrementStaticReferenceCountIfIncluded(source, target)
    }

    fun getStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): AggregateData {
        return incrementStaticReferenceCountIfIncluded(source, target)
    }


    fun putStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): AggregateData {
        return incrementStaticReferenceCountIfIncluded(source, target)
    }

    fun newInstance(
        source: QualifiedMethod,
        targetClassName: String
    ): AggregateData {
        return incrementStaticReferenceCountIfIncluded(source, targetClassName)
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
        return copy(classDataMap = newMap)
    }

    private fun updateMethods(matchEnum: MatchEnum, qualifiedMethod: QualifiedMethod): AggregateData {
        val oldSet = methods[matchEnum] ?: emptySet()
        val newSet = oldSet + qualifiedMethod
        val newMap = methods + (matchEnum to newSet)
        return copy(methods = newMap)
    }

    private fun updateClasses(matchEnum: MatchEnum, className: String): AggregateData {
        val oldSet = classes[matchEnum] ?: emptySet()
        val newSet = oldSet + className
        val newMap = classes + (matchEnum to newSet)
        return copy(classes = newMap)
    }

    private fun incrementStaticReferenceCountIfIncluded(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): AggregateData {
        val matchEnum = acceptMethod(target)
        val a = updateMethods(matchEnum, target)
        return if (matchEnum == MatchEnum.INCLUDED) {
            val key = source.toAggregateKey()
            a.updateEntry(key) { classData ->
                classData.addToStaticReferenceCount(1)
            }
        } else {
            a
        }
    }

    private fun incrementStaticReferenceCountIfIncluded(source: QualifiedMethod, target: String): AggregateData {
        val matchEnum = acceptClass(target)
        val a = updateClasses(matchEnum, target)
        return if (matchEnum == MatchEnum.INCLUDED) {
            val key = source.toAggregateKey()
            a.updateEntry(key) { classData ->
                classData.addToStaticReferenceCount(1)
            }
        } else {
            a
        }
    }

    fun summaryMethodNames(matchEnum: MatchEnum): List<String> {
        val set = methods[matchEnum] ?: emptySet()
        return set.map { it.regexMatchKey() }.toList().sorted()
    }

    fun summaryClassNames(matchEnum: MatchEnum): List<String> {
        val set = classes[matchEnum] ?: emptySet()
        return set.toList().sorted()
    }

    companion object {
        fun create(
            acceptMethod: (QualifiedMethod) -> MatchEnum,
            acceptClass: (String) -> MatchEnum
        ) = AggregateData(
            emptyMap(),
            acceptMethod,
            acceptClass,
            emptyMap(),
            emptyMap()
        )
    }
}
