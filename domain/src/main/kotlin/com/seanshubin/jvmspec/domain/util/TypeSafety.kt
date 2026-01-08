package com.seanshubin.jvmspec.domain.util

object TypeSafety {
    inline fun <reified T> Any?.toTypedList(): List<T> {
        return (this as List<*>).map { it as T }
    }
}
