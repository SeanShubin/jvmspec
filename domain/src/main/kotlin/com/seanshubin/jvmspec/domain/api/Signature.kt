package com.seanshubin.jvmspec.domain.api

data class Signature(
    val descriptor: String,
    val parameters: List<SignatureType>?,
    val returnType: SignatureType
) {
    fun javaFormat(className: String, methodName: String): String {
        val parameterList = parameters?.joinToString(", ", "(", ")") { it.toJavaClassName() } ?: ""
        val javaClassName = className.toJavaClassName()
        val formattedReturnType = returnType.toJavaClassName()
        val formatted = "$formattedReturnType $javaClassName.$methodName$parameterList"
        return formatted
    }

    fun javaFormatUnqualified(className: String, methodName: String): String {
        val parameterList = parameters?.joinToString(", ", "(", ")") { it.toUnqualifiedJavaClassName() } ?: ""
        val javaClassName = className.removePrefix()
        val formattedReturnType = returnType.toUnqualifiedJavaClassName()
        val formatted = "$formattedReturnType $javaClassName.$methodName$parameterList"
        return formatted
    }

    private fun SignatureType.toJavaClassName(): String {
        val namePart = name.toJavaClassName()
        val arrayPart = "[]".repeat(dimensions)
        return namePart + arrayPart
    }

    private fun String.toJavaClassName(): String {
        return this.removeJavaLangPrefix().replace("/", ".")
    }

    private fun String.removeJavaLangPrefix(): String {
        return if (this.startsWith("java/lang/")) this.substring("java/lang/".length) else this
    }

    private fun SignatureType.toUnqualifiedJavaClassName(): String {
        val namePart = name.removePrefix()
        val arrayPart = "[]".repeat(dimensions)
        return namePart + arrayPart
    }

    private fun String.removePrefix(): String {
        val index = this.lastIndexOf('/')
        return if (index == -1) this else this.substring(index + 1)
    }
}
