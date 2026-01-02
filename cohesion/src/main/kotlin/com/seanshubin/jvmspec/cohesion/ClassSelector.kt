package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.data.ClassFile

interface ClassSelector {
    fun <T> flatMap(f: (ClassFile) -> List<T>): List<T>
}
