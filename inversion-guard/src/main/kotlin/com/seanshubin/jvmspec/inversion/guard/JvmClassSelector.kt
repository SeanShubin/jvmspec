package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.domain.jvm.JvmClass

interface JvmClassSelector {
    fun <T> flatMap(f: (JvmClass) -> List<T>): List<T>
}