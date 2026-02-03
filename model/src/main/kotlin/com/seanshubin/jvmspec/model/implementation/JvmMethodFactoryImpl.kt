package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.MethodInfo
import com.seanshubin.jvmspec.model.api.JvmAttributeFactory
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmMethod
import com.seanshubin.jvmspec.model.api.JvmMethodFactory

class JvmMethodFactoryImpl(
    private val attributeFactory: JvmAttributeFactory
) : JvmMethodFactory {
    override fun createMethod(jvmClass: JvmClass, methodInfo: MethodInfo): JvmMethod =
        JvmMethodImpl(jvmClass, methodInfo, attributeFactory)
}
