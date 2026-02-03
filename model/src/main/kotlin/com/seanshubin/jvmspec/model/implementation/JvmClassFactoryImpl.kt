package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.ClassFile
import com.seanshubin.jvmspec.model.api.*

class JvmClassFactoryImpl(
    private val methodFactory: JvmMethodFactory,
    private val fieldFactory: JvmFieldFactory,
    private val attributeFactory: JvmAttributeFactory
) : JvmClassFactory {
    override fun createClass(classFile: ClassFile): JvmClass =
        JvmClassImpl(classFile, methodFactory, fieldFactory, attributeFactory)
}
