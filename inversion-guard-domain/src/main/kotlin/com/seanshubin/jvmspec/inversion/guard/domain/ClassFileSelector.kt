package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.data.ClassFile

interface ClassFileSelector {
    fun <T> flatMap(f: (ClassFile) -> List<T>): List<T>
}
