package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.ClassFile

interface JvmClassFactory {
    fun createClass(classFile: ClassFile): JvmClass
}
