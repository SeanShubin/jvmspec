package com.seanshubin.jvmspec.domain.aggregation

data class ClassData(
    val name: String,
    val staticReferenceCount: Int,
    val cyclomaticComplexity: Int
) {
    fun addToStaticReferenceCount(increment: Int): ClassData =
        copy(staticReferenceCount = staticReferenceCount + increment)

    fun addToCyclomaticComplexity(increment: Int): ClassData =
        copy(cyclomaticComplexity = cyclomaticComplexity + increment)

    fun toLine(): String = "[static=$staticReferenceCount, complexity=$cyclomaticComplexity] $name"

    companion object {
        fun create(name: String): ClassData = ClassData(name, 0, 0)
    }
}
