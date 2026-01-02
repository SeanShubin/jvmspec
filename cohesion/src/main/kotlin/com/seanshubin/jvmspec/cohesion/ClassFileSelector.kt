package com.seanshubin.jvmspec.cohesion

import com.seanshubin.jvmspec.domain.data.ClassFile

interface ClassFileSelector {
    fun <T> flatMap(f: (ClassFile) -> List<T>): List<T>
}
