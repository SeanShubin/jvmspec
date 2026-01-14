package com.seanshubin.jvmspec.domain.model.api

interface JvmAttribute {
    fun name(): String
    fun bytes(): List<Byte>
}
