package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.conversion.Converter.toJvmClass

class JvmClassSelectorImpl(
    private val classSelector: ClassSelector
) : JvmClassSelector {
    override fun <T> flatMap(f: (JvmClass) -> List<T>): List<T> {
        return classSelector.flatMap { f(it.toJvmClass()) }
    }
}
