package com.seanshubin.jvmspec.domain.aggregation

data class ClassData(
    val classBaseName: String,
    val staticReferenceCount: Int,
    val cyclomaticComplexity: Int,
    val newInstanceCount: Int
) {
    fun addToStaticReferenceCount(
        source: QualifiedMethod,
        target: QualifiedMethod
    ): ClassData =
        copy(staticReferenceCount = staticReferenceCount + 1)

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

    companion object {
        fun create(name: String): ClassData = ClassData(name, 0, 0, 0)
    }
}
