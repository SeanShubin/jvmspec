package com.seanshubin.jvmspec.model.api

interface JvmAttribute {
    fun name(): String
    fun bytes(): List<Byte>
}
