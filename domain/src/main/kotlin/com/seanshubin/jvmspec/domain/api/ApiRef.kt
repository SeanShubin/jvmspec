package com.seanshubin.jvmspec.domain.api

data class ApiRef(
    val className: String,
    val name: String,
    val signature: Signature
) {
    fun methodId(): String {
        return "$className:$name:${signature.descriptor}"
    }
}
