package com.seanshubin.jvmspec.domain.api

interface ApiMethod {
    fun name(): String
    fun signature(): Signature
    fun code(): ApiCodeAttribute
    fun attributes(): List<ApiAttribute>
}
