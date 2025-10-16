package com.seanshubin.jvmspec.domain.aggregation

import com.seanshubin.jvmspec.domain.api.ApiClass
import com.seanshubin.jvmspec.domain.api.ApiMethod
import com.seanshubin.jvmspec.domain.api.ApiRef
import com.seanshubin.jvmspec.domain.util.MatchEnum

interface Aggregator {
    fun classFile(classFile: ApiClass)
    fun invokeStatic(source: ApiMethod, target: ApiRef)
    fun getStatic(source: ApiMethod, target: ApiRef)
    fun putStatic(source: ApiMethod, target: ApiRef)
    fun newInstance(source: ApiMethod, targetClassName: String)
    fun cyclomaticComplexity(source: ApiMethod, complexity: Int)
    fun summaryDescendingCyclomaticComplexity(): List<String>
    fun summaryDescendingStaticReferenceCount(): List<String>
    fun summaryMethodNames(matchEnum: MatchEnum): List<String>
    fun summaryClassNames(matchEnum: MatchEnum): List<String>
    fun summaryOrigin(): List<String>
    fun summaryTotal(): List<String>
    fun methodCategories(method: ApiMethod, categories: Set<String>)
}
