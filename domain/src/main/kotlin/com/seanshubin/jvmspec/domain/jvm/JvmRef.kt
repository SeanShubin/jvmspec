package com.seanshubin.jvmspec.domain.jvm

data class JvmRef(
    val className: String,
    val name: String,
    val signature: Signature
) {
    fun methodId(): String {
        return "$className:$name:${signature.descriptor}"
    }

    fun javaFormat(): String {
        return signature.javaFormat(className, name)
    }
}
