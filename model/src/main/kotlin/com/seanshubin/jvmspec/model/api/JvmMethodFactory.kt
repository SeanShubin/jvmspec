package com.seanshubin.jvmspec.model.api

import com.seanshubin.jvmspec.classfile.structure.MethodInfo

interface JvmMethodFactory {
    fun createMethod(jvmClass: JvmClass, methodInfo: MethodInfo): JvmMethod
}
