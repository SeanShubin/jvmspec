package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.jvm.JvmClass

interface JvmClassSelector {
    fun <T> flatMap(f: (JvmClass) -> List<T>): List<T>
}