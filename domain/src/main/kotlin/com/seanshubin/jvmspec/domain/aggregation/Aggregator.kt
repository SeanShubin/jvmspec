package com.seanshubin.jvmspec.domain.aggregation

import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.jvm.JvmMethod
import com.seanshubin.jvmspec.domain.jvm.JvmRef
import com.seanshubin.jvmspec.domain.util.MatchEnum

interface Aggregator {
    fun classFile(classFile: JvmClass)
    fun invokeStatic(source: JvmMethod, target: JvmRef)
    fun getStatic(source: JvmMethod, target: JvmRef)
    fun putStatic(source: JvmMethod, target: JvmRef)
    fun newInstance(source: JvmMethod, targetClassName: String)
    fun cyclomaticComplexity(source: JvmMethod, complexity: Int)
    fun summaryDescendingCyclomaticComplexity(): List<String>
    fun summaryDescendingStaticReferenceCount(): List<String>
    fun summaryMethodNames(matchEnum: MatchEnum): List<String>
    fun summaryClassNames(matchEnum: MatchEnum): List<String>
    fun summaryOrigin(): List<String>
    fun summaryTotal(): List<String>
    fun methodCategories(method: JvmMethod, categories: Set<String>)
}
