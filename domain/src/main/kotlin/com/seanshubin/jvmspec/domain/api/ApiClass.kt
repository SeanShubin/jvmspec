package com.seanshubin.jvmspec.domain.api

interface ApiClass {
    fun origin(): String
    fun className(): String
    fun methods(): List<ApiMethod>
    fun disassemblyLines(): List<String>
}
