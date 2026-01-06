package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.domain.data.ClassFile

interface ClassSelector {
    fun <T> flatMap(f: (ClassFile) -> List<T>): List<T>
}
