package com.seanshubin.jvmspec.inversion.guard

import com.seanshubin.jvmspec.domain.data.ClassFile

class ClassSelectorImpl(

) : ClassSelector {
    override fun <T> flatMap(f: (ClassFile) -> List<T>): List<T> {
        throw UnsupportedOperationException("Not Implemented!")
    }
}