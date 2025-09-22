package com.seanshubin.jvmspec.domain.aggregation

import com.seanshubin.jvmspec.domain.data.ClassFile
import com.seanshubin.jvmspec.domain.util.MatchEnum

interface Aggregator {
    fun classFile(classFile: ClassFile)
    fun invokeStatic(source: QualifiedMethod, target: QualifiedMethod)
    fun getStatic(source: QualifiedMethod, target: QualifiedMethod)
    fun putStatic(source: QualifiedMethod, target: QualifiedMethod)
    fun newInstance(source: QualifiedMethod, targetClassName: String)
    fun cyclomaticComplexity(qualifiedMethod: QualifiedMethod, complexity: Int)
    fun summaryDescendingCyclomaticComplexity(): List<String>
    fun summaryDescendingStaticReferenceCount(): List<String>
    fun summaryMethodNames(matchEnum: MatchEnum): List<String>
    fun summaryClassNames(matchEnum: MatchEnum): List<String>
    fun summaryOrigin(): List<String>
}
