package com.seanshubin.jvmspec.domain.aggregation

import com.seanshubin.jvmspec.domain.api.ApiMethod
import com.seanshubin.jvmspec.domain.api.ApiRef

data class ClassData(
    val classBaseName: String,
    val staticReferenceCount: Int,
    val cyclomaticComplexity: Int,
    val newInstanceCount: Int,
    val staticInvocations: Map<ApiRef, Int>,
    val methodCategories: Map<String, Set<String>>
) {
    fun staticsAllowed(): Boolean {
        val allCategories = methodCategories.values.toSet()
        val result = if (allCategories.isEmpty()) false else if (allCategories.all {
                it.size == 1
            }) true else false
        if (!result && !allCategories.isEmpty()) {
            println("$classBaseName $allCategories")
        }
        return result
    }

    val uniqueStaticReferenceCount: Int get() = staticInvocations.size
    fun addToStaticReferenceCount(
        source: ApiMethod,
        target: ApiRef
    ): ClassData =
        copy(
            staticReferenceCount = staticReferenceCount + 1,
            staticInvocations = staticInvocations + (target to (staticInvocations[target] ?: 0) + 1)
        )

    fun addToNewInstanceCount(
        source: ApiMethod,
        target: String
    ): ClassData =
        copy(newInstanceCount = newInstanceCount + 1)

    fun addToCyclomaticComplexity(
        source: ApiMethod,
        complexity: Int
    ): ClassData =
        copy(cyclomaticComplexity = cyclomaticComplexity + complexity)

    fun toLine(): String =
        "[statics-allowed=${staticsAllowed()} static=$staticReferenceCount, complexity=$cyclomaticComplexity] $classBaseName"

    fun toStaticInvocationLines(): List<String> {
        val header = listOf(toLine())
        val staticHeader = listOf("  quantity, static invocation")
        val staticInvocations = staticInvocations.toList().sortedByDescending { (_, quantity) ->
            quantity
        }.map { (method, quantity) ->
            "    ($quantity) ${method.signature.javaFormat(method.className, method.name)}"
        }
        val categoryHeader = listOf("  categories, method")
        val methodCategoryLines = methodCategories.map { (methodId, categories) ->
            "    $categories $methodId"
        }
        return header + staticHeader + staticInvocations + categoryHeader + methodCategoryLines
    }

    fun updateMethodCategories(method: ApiMethod, categories: Set<String>): ClassData {
        val id = method.ref().methodId()
        val oldCategories = methodCategories[id] ?: emptySet()
        val newCategories = oldCategories + categories
        val newMethodCategories = methodCategories + (id to newCategories)
        return copy(methodCategories = newMethodCategories)
    }

    companion object {
        fun create(name: String): ClassData = ClassData(
            classBaseName = name,
            staticReferenceCount = 0,
            cyclomaticComplexity = 0,
            newInstanceCount = 0,
            staticInvocations = emptyMap(),
            methodCategories = emptyMap()
        )
    }
}
