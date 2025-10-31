package com.seanshubin.jvmspec.domain.jvm

interface JvmMethod : JvmFieldOrMethod {
    fun code(): JvmCodeAttribute?
}
