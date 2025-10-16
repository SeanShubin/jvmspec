package com.seanshubin.jvmspec.domain.api

interface ApiAttribute {
    fun name(): String
    fun bytes(): List<Byte>
    fun asCodeAttribute(): ApiCodeAttribute
}
