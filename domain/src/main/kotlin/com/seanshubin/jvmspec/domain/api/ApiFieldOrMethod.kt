package com.seanshubin.jvmspec.domain.api

import com.seanshubin.jvmspec.domain.primitive.AccessFlag

interface ApiFieldOrMethod {
    fun className(): String
    fun name(): String
    fun signature(): Signature
    fun attributes(): List<ApiAttribute>
    fun accessFlags(): Set<AccessFlag>
    fun ref(): ApiRef {
        return ApiRef(className(), name(), signature())
    }
}
