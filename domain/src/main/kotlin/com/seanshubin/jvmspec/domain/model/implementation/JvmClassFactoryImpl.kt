package com.seanshubin.jvmspec.domain.model.implementation

import com.seanshubin.jvmspec.domain.classfile.structure.ClassFile
import com.seanshubin.jvmspec.domain.model.api.*

class JvmClassFactoryImpl(
    private val methodFactory: JvmMethodFactory,
    private val fieldFactory: JvmFieldFactory,
    private val attributeFactory: JvmAttributeFactory
) : JvmClassFactory {
    override fun createClass(classFile: ClassFile): JvmClass =
        JvmClassImpl(classFile, methodFactory, fieldFactory, attributeFactory)
}
