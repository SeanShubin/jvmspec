package com.seanshubin.jvmspec.model.api

interface JvmSignatureAttribute : JvmAttribute {
    fun signature(): String
    val signatureIndex: UShort
}
