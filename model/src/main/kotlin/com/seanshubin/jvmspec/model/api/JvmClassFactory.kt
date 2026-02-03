package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.ClassFile

interface JvmClassFactory {
    fun createClass(classFile: ClassFile): JvmClass
}
