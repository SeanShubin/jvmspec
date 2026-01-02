package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.jvm.JvmClass

interface JvmClassSelector {
    fun <T> flatMap(f: (JvmClass) -> List<T>): List<T>
}