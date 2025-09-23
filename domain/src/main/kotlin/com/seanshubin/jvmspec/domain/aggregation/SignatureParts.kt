package com.seanshubin.jvmspec.domain.aggregation

data class SignatureParts(
    val parameters: List<SignatureType>?,
    val returnType: SignatureType
) {
}
