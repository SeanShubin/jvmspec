package com.seanshubin.jvmspec.domain.model.api

interface JvmSignatureAttribute : JvmAttribute {
    fun signature(): String
    val signatureIndex: UShort
}
