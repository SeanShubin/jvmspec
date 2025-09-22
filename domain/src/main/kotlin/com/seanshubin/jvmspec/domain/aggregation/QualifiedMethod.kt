package com.seanshubin.jvmspec.domain.aggregation

data class QualifiedMethod(
    val className: String,
    val methodName: String,
    val methodDescriptor: String
) {
    fun classBaseName(): String {
        val indexOfDollar = className.indexOf('$')
        val key = if (indexOfDollar == -1) className else className.take(indexOfDollar)
        return key
    }
    fun id(): String = "$className:$methodName:$methodDescriptor"
}
