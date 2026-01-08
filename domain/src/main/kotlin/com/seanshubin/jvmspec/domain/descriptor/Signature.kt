package com.seanshubin.jvmspec.domain.descriptor

data class Signature(
    val className: String,
    val methodName: String,
    val descriptor: Descriptor,
) {
    fun javaFormat(): String {
        return descriptor.javaFormat(className, methodName)
    }

    fun compactFormat(): String {
        return descriptor.compactFormat(className, methodName)
    }

    fun javaFormatUnqualified(): String {
        return descriptor.javaFormatUnqualified(className, methodName)
    }
}
