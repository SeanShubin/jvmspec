package com.seanshubin.jvmspec.domain.infrastructure.types

object TypeSafety {
    inline fun <reified T> Any?.toTypedList(): List<T> {
        return (this as List<*>).map { it as T }
    }
}
