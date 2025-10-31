package com.seanshubin.jvmspec.domain.aggregation

import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.jvm.JvmMethod
import com.seanshubin.jvmspec.domain.jvm.JvmRef
import com.seanshubin.jvmspec.domain.util.MatchEnum

data class AggregateData(
    val classDataMap: Map<String, ClassData>,
    val acceptMethod: (JvmRef) -> MatchEnum,
    val acceptClass: (String) -> MatchEnum,
    val methods: Map<MatchEnum, Set<JvmRef>>,
    val classes: Map<MatchEnum, Set<String>>,
    val mapClassNameOriginIds: Map<String, List<String>>
) {
    fun classFile(classFile: JvmClass): AggregateData {
        val originId = classFile.origin
        val className = classFile.thisClassName
        val oldOriginIds = mapClassNameOriginIds[className] ?: emptyList()
        if (oldOriginIds.contains(originId)) throw RuntimeException("Duplicate origin $originId for $className")
        val newOriginIds = oldOriginIds + originId
        val newMapClassNameOriginIds = mapClassNameOriginIds + (className to newOriginIds)
        return copy(mapClassNameOriginIds = newMapClassNameOriginIds)
    }

    fun invokeStatic(
        source: JvmMethod,
        target: JvmRef
    ): AggregateData {
        return incrementStaticReferenceCountIfOnBlacklist(source, target)
    }

    fun getStatic(
        source: JvmMethod,
        target: JvmRef
    ): AggregateData {
        return incrementStaticReferenceCountIfOnBlacklist(source, target)
    }


    fun putStatic(
        source: JvmMethod,
        target: JvmRef
    ): AggregateData {
        return incrementStaticReferenceCountIfOnBlacklist(source, target)
    }

    fun newInstance(
        source: JvmMethod,
        targetClassName: String
    ): AggregateData {
        return incrementStaticReferenceCountIfOnBlacklist(source, targetClassName)
    }

    fun cyclomaticComplexity(
        source: JvmMethod,
        complexity: Int
    ): AggregateData {
        val key = source.className().classBaseName()
        return updateEntry(key) { classData ->
            classData.addToCyclomaticComplexity(source, complexity)
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
        }.sortedByDescending {
            it.uniqueStaticReferenceCount
        }.sortedBy {
            it.staticsAllowed()
        }.flatMap { it.toStaticInvocationLines() }
    }

    fun totals(): List<String> {
        val violationCount = classDataMap.count { (name, classData) ->
            !classData.staticsAllowed() && classData.staticReferenceCount > 0
        }
        val okCount = classDataMap.count { (name, classData) ->
            classData.staticsAllowed() || classData.staticReferenceCount == 0
        }
        val totalCount = classDataMap.size
        val percentage = violationCount.toDouble() * 100 / totalCount
        val percentageFormatted = String.format("%.2f", percentage)
        return listOf(
            "violations: $violationCount",
            "ok        : $okCount",
            "total     : $totalCount",
            "percentage: $percentageFormatted%"
        )
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

    private fun updateMethods(matchEnum: MatchEnum, qualifiedMethod: JvmRef): AggregateData {
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
        source: JvmMethod,
        target: JvmRef
    ): AggregateData {
        val matchEnum = acceptMethod(target)
        val a = updateMethods(matchEnum, target)
        return if (matchEnum == MatchEnum.BLACKLIST_ONLY) {
            val key = source.className().classBaseName()
            a.updateEntry(key) { classData ->
                classData.addToStaticReferenceCount(source, target)
            }
        } else {
            a
        }
    }

    private fun incrementStaticReferenceCountIfOnBlacklist(source: JvmMethod, target: String): AggregateData {
        val matchEnum = acceptClass(target)
        val a = updateClasses(matchEnum, target)
        return if (matchEnum == MatchEnum.BLACKLIST_ONLY) {
            val key = source.className()
            a.updateEntry(key) { classData ->
                classData.addToNewInstanceCount(source, target)
            }
        } else {
            a
        }
    }

    fun summaryMethodNames(matchEnum: MatchEnum): List<String> {
        val set = methods[matchEnum] ?: emptySet()
        return set.map { it.methodId() }.toList().sorted()
    }

    fun summaryClassNames(matchEnum: MatchEnum): List<String> {
        val set = classes[matchEnum] ?: emptySet()
        return set.toList().sorted()
    }

    fun methodCategories(method: JvmMethod, categories: Set<String>): AggregateData {
        val key = method.className().classBaseName()
        return updateEntry(key) { classData ->
            classData.updateMethodCategories(method, categories)
        }
    }

    companion object {
        fun create(
            acceptMethod: (JvmRef) -> MatchEnum,
            acceptClass: (String) -> MatchEnum
        ) = AggregateData(
            emptyMap(),
            acceptMethod,
            acceptClass,
            emptyMap(),
            emptyMap(),
            emptyMap()
        )

        fun String.classBaseName(): String {
            val indexOfDollar = this.indexOf('$')
            val key = if (indexOfDollar == -1) this else this.take(indexOfDollar)
            return key
        }
    }
}
