package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.classfile.structure.ClassFile

interface ClassFileSelector {
    fun <T> flatMap(f: (ClassFile) -> List<T>): List<T>
}
