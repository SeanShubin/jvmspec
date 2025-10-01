package com.seanshubin.jvmspec.domain.api

interface ApiAttribute {
    fun name(): String
    fun asCodeAttribute(): ApiCodeAttribute
}
