package com.seanshubin.jvmspec.domain.jvm

interface JvmAttribute {
    fun name(): String
    fun bytes(): List<Byte>
}
