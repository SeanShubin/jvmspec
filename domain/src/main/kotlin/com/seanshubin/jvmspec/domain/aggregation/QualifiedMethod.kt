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
    fun javaSignature(): String {
        val signatureParts = DescriptorBuilderState.build(methodDescriptor)
        val returnType = signatureParts.returnType.toJavaClassName()
        val signatureParameters = signatureParts.parameters
        val parameterList = if (signatureParameters == null) {
            ""
        } else {
            signatureParts.parameters.joinToString(", ", "(", ")") { it.toJavaClassName() }
        }
        val javaClassName = className.toJavaClassName()
        return "$returnType $javaClassName.$methodName$parameterList"
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
}
