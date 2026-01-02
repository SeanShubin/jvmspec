package com.seanshubin.jvmspec.domain.aggregation

import com.seanshubin.jvmspec.domain.jvm.JvmClass
import com.seanshubin.jvmspec.domain.jvm.JvmMethod
import com.seanshubin.jvmspec.domain.jvm.JvmRef
import com.seanshubin.jvmspec.domain.util.FilterResult

interface Aggregator {
    fun classFile(classFile: JvmClass)
    fun invokeStatic(source: JvmMethod, target: JvmRef)
    fun getStatic(source: JvmMethod, target: JvmRef)
    fun putStatic(source: JvmMethod, target: JvmRef)
    fun newInstance(source: JvmMethod, targetClassName: String)
    fun cyclomaticComplexity(source: JvmMethod, complexity: Int)
    fun summaryDescendingCyclomaticComplexity(): List<String>
    fun summaryDescendingStaticReferenceCount(): List<String>
    fun summaryMethodNames(filterResult: FilterResult): List<String>
    fun summaryClassNames(filterResult: FilterResult): List<String>
    fun summaryOrigin(): List<String>
    fun summaryTotal(): List<String>
    fun methodCategories(method: JvmMethod, categories: Set<String>)
}
