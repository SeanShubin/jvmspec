package com.seanshubin.jvmspec.model.implementation

import com.seanshubin.jvmspec.classfile.structure.FieldInfo
import com.seanshubin.jvmspec.model.api.JvmAttributeFactory
import com.seanshubin.jvmspec.model.api.JvmClass
import com.seanshubin.jvmspec.model.api.JvmField
import com.seanshubin.jvmspec.model.api.JvmFieldFactory

class JvmFieldFactoryImpl(
    private val attributeFactory: JvmAttributeFactory
) : JvmFieldFactory {
    override fun createField(jvmClass: JvmClass, fieldInfo: FieldInfo): JvmField =
        JvmFieldImpl(jvmClass, fieldInfo, attributeFactory)
}
