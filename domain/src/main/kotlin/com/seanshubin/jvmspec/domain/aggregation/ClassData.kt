package com.seanshubin.jvmspec.domain.aggregation

data class ClassData(
    val classBaseName: String,
    val staticReferenceCount: Int,
    val cyclomaticComplexity: Int,
    val newInstanceCount: Int,
    val staticInvocations: Map<QualifiedMethod, Int>
) {
    val uniqueStaticReferenceCount: Int get() = staticInvocations.size
    fun addToStaticReferenceCount(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): ClassData =
        copy(
            staticReferenceCount = staticReferenceCount + 1,
            staticInvocations = staticInvocations + (target to (staticInvocations[target] ?: 0) + 1)
        )

    fun addToNewInstanceCount(
        source: QualifiedMethod,
        target: String
    ): ClassData =
        copy(newInstanceCount = newInstanceCount + 1)

    fun addToCyclomaticComplexity(
        qualifiedMethod: QualifiedMethod,
        complexity: Int
    ): ClassData =
        copy(cyclomaticComplexity = cyclomaticComplexity + complexity)

    fun toLine(): String = "[static=$staticReferenceCount, complexity=$cyclomaticComplexity] $classBaseName"

    fun toStaticInvocationLines(): List<String> {
        val header = listOf(toLine())
        val ids = staticInvocations.toList().sortedByDescending { (_, quantity) ->
            quantity
        }.map { (method, quantity) ->
            "  ($quantity) ${method.javaSignature()}"
        }
        return header + ids
    }

    companion object {
        fun create(name: String): ClassData = ClassData(
            classBaseName = name,
            staticReferenceCount = 0,
            cyclomaticComplexity = 0,
            newInstanceCount = 0,
            staticInvocations = emptyMap()
        )
    }
}
