package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.converter.toJvmClass
import com.seanshubin.jvmspec.domain.jvm.JvmClass

class JvmClassSelectorImpl(
    private val classSelector: ClassSelector
) : JvmClassSelector {
    override fun <T> flatMap(f: (JvmClass) -> List<T>): List<T> {
        return classSelector.flatMap { f(it.toJvmClass()) }
    }
}
