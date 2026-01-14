package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.MethodInfo
import com.seanshubin.jvmspec.domain.model.api.JvmAttributeFactory
import com.seanshubin.jvmspec.domain.model.api.JvmClass
import com.seanshubin.jvmspec.domain.model.api.JvmMethod
import com.seanshubin.jvmspec.domain.model.api.JvmMethodFactory

class JvmMethodFactoryImpl(
    private val attributeFactory: JvmAttributeFactory
) : JvmMethodFactory {
    override fun createMethod(jvmClass: JvmClass, methodInfo: MethodInfo): JvmMethod =
        JvmMethodImpl(jvmClass, methodInfo, attributeFactory)
}
