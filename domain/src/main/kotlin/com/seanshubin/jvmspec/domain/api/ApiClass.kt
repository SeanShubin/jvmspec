package com.seanshubin.jvmspec.domain.api

interface ApiClass {
    fun className(): String
    fun methods(): List<ApiMethod>
}
