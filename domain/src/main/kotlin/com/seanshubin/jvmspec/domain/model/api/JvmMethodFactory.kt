package com.seanshubin.jvmspec.domain.model.api

import com.seanshubin.jvmspec.domain.classfile.structure.MethodInfo

interface JvmMethodFactory {
    fun createMethod(jvmClass: JvmClass, methodInfo: MethodInfo): JvmMethod
}
