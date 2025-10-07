package com.seanshubin.jvmspec.domain.api

interface ApiMethod {
    fun className(): String
    fun name(): String
    fun signature(): Signature
    fun code(): ApiCodeAttribute?
    fun attributes(): List<ApiAttribute>
    fun ref(): ApiRef {
        return ApiRef(className(), name(), signature())
    }
}
