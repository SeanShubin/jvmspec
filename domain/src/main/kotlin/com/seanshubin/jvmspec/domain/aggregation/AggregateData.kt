package com.seanshubin.jvmspec.domain.aggregation

import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.util.MatchEnum

data class AggregateData(
    val classDataMap: Map<String, ClassData>,
    val acceptMethod: (QualifiedMethod) -> MatchEnum,
    val acceptClass: (String) -> MatchEnum,
    val methods: Map<MatchEnum, Set<QualifiedMethod>>,
    val classes: Map<MatchEnum, Set<String>>,
    val mapClassNameOriginIds: Map<String, List<String>>
) {
    fun classFile(classFile: ClassFile): AggregateData {
        val originId = classFile.origin.id
        val className = classFile.thisClassName()
        val oldOriginIds = mapClassNameOriginIds[className] ?: emptyList()
        if (oldOriginIds.contains(originId)) throw RuntimeException("Duplicate origin $originId for $className")
        val newOriginIds = oldOriginIds + originId
        val newMapClassNameOriginIds = mapClassNameOriginIds + (className to newOriginIds)
        return copy(mapClassNameOriginIds = newMapClassNameOriginIds)
    }
    fun invokeStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): AggregateData {
        return incrementStaticReferenceCountIfOnBlacklist(source, target)
    }

    fun getStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): AggregateData {
        return incrementStaticReferenceCountIfOnBlacklist(source, target)
    }


    fun putStatic(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): AggregateData {
        return incrementStaticReferenceCountIfOnBlacklist(source, target)
    }

    fun newInstance(
        source: QualifiedMethod,
        targetClassName: String
    ): AggregateData {
        return incrementStaticReferenceCountIfOnBlacklist(source, targetClassName)
    }

    fun cyclomaticComplexity(
        qualifiedMethod: QualifiedMethod,
        complexity: Int
    ): AggregateData {
        val key = qualifiedMethod.classBaseName()
        return updateEntry(key) { classData ->
            classData.addToCyclomaticComplexity(qualifiedMethod, complexity)
        }
    }

    fun summaryDescendingCyclomaticComplexity(): List<String> {
        return classDataMap.values.sortedBy {
            it.classBaseName
        }.sortedByDescending {
            it.staticReferenceCount
        }.sortedByDescending {
            it.cyclomaticComplexity
        }.map { it.toLine() }
    }

    fun summaryDescendingStaticReferenceCount(): List<String> {
        return classDataMap.values.sortedBy {
            it.classBaseName
        }.sortedByDescending {
            it.cyclomaticComplexity
        }.sortedByDescending {
            it.staticReferenceCount
        }.map { it.toLine() }
    }

    fun summaryOrigin(): List<String> {
        val classNames = mapClassNameOriginIds.keys.sorted()
        return classNames.flatMap { className ->
            val originIds = mapClassNameOriginIds.getValue(className)
            val originIdCount = originIds.size
            val classNameLine = "[$originIdCount] $className"
            val originIdLines = originIds.map { originId ->
                "  $originId"
            }
            listOf(classNameLine) + originIdLines
        }
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

    private fun incrementStaticReferenceCountIfOnBlacklist(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): AggregateData {
        val matchEnum = acceptMethod(target)
        val a = updateMethods(matchEnum, target)
        return if (matchEnum == MatchEnum.BLACKLIST_ONLY) {
            val key = source.classBaseName()
            a.updateEntry(key) { classData ->
                classData.addToStaticReferenceCount(source, target)
            }
        } else {
            a
        }
    }

    private fun incrementStaticReferenceCountIfOnBlacklist(source: QualifiedMethod, target: String): AggregateData {
        val matchEnum = acceptClass(target)
        val a = updateClasses(matchEnum, target)
        return if (matchEnum == MatchEnum.BLACKLIST_ONLY) {
            val key = source.classBaseName()
            a.updateEntry(key) { classData ->
                classData.addToNewInstanceCount(source, target)
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
            emptyMap(),
            emptyMap()
        )
    }
}
