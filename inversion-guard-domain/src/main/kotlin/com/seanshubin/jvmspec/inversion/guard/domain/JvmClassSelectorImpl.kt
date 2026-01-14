package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.conversion.Converter

class JvmClassSelectorImpl(
    private val classSelector: ClassSelector,
    private val converter: Converter
) : JvmClassSelector {
    override fun <T> flatMap(f: (JvmClass) -> List<T>): List<T> {
        return classSelector.flatMap { classFile ->
            val jvmClass = with(converter) { classFile.toJvmClass() }
            f(jvmClass)
        }
    }
}
