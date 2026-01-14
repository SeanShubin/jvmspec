package com.seanshubin.jvmspec.inversion.guard.domain

import com.seanshubin.jvmspec.domain.classfile.structure.ClassFile

class ClassSelectorImpl(

) : ClassSelector {
    override fun <T> flatMap(f: (ClassFile) -> List<T>): List<T> {
        throw UnsupportedOperationException("Not Implemented!")
    }
}